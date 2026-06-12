package nl.hkstwk.calculationmodule.controllers;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.exceptions.RateLimitExceededException;
import nl.hkstwk.calculationmodule.mappers.CompoundInterestMapper;
import nl.hkstwk.calculationmodule.services.BucketService;
import nl.hkstwk.calculationmodule.services.CalculationRequestService;
import nl.hkstwk.calculationmodule.services.InterestService;
import nl.hkstwk.calculationmodule.services.OrderService;
import nl.hkstwk.calculationmodule.services.UserService;
import nl.hkstwk.calculationmodule.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/interest")
public class InterestController {

    private final InterestService interestService;
    private final CalculationRequestService calculationRequestService;
    private final CompoundInterestMapper compoundInterestMapper;
    private final UserService userService;
    private final OrderService orderService;
    private final BucketService bucketService;

    @PostMapping("/compound")
    public ResponseEntity<CompoundInterestResponseDto> compoundInterestCalculation(@Valid @RequestBody CompoundInterestRequestDto compoundInterestRequestDto) {
        CalculationTypeEnum calculationType = compoundInterestRequestDto.getIncludeDetails() ? CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS : CalculationTypeEnum.COMPOUND_INTEREST;
        userService.findCreateOrUpdateUser(UserUtil.fetchCurrentUserFromContext().getUsername());
        calculationRequestService.saveRequest(compoundInterestMapper.toEntity(compoundInterestRequestDto, calculationType));
        CompoundInterestResponseDto responseDto = interestService.compoundInterestCalculation(compoundInterestRequestDto);
        orderService.completeOrder(responseDto, "SMS");
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/resource")
    public ResponseEntity<String> getResource(HttpServletRequest request) {
        String key = "test-bucket";

        Bucket bucket = bucketService.resolveBucket(key);

        if (!bucket.tryConsume(1)) {
            throw new RateLimitExceededException(bucket.estimateAbilityToConsume(1)
                    .getNanosToWaitForRefill() / 1_000_000_000);
        }

        return ResponseEntity.ok("Yeah you're good to go!! Available tokens:    " + bucket.getAvailableTokens());
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ProblemDetail> handleRateLimit(RateLimitExceededException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.TOO_MANY_REQUESTS,
                "Rate limit overschreden"
        );
        problem.setProperty("retryAfterSeconds", ex.getRetryAfterSeconds());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-Rate-Limit-Retry-After-Seconds", String.valueOf(ex.getRetryAfterSeconds()))
                .body(problem);
    }
}
