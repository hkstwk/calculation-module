# [1.4.0](https://github.com/hkstwk/calculation-module/compare/v1.3.1...v1.4.0) (2025-10-21)


### Features

* **aop:** implemented logging aspect for controllers, services, and calculators ([c850438](https://github.com/hkstwk/calculation-module/commit/c8504383e7f6e80cd03c874c3423a6ebe1ed9e49))

## [1.3.1](https://github.com/hkstwk/calculation-module/compare/v1.3.0...v1.3.1) (2025-09-27)


### Bug Fixes

* **deps:** update all maven dependencies ([8d46d7e](https://github.com/hkstwk/calculation-module/commit/8d46d7e1aa5462007537934f6f740cee2120e98e))
* **it:** pinned mysql testcontainer version; added resource loader helper method ([a853fe3](https://github.com/hkstwk/calculation-module/commit/a853fe32925fc01e6b0cc0e2049859bfadf9b817))

# [1.3.0](https://github.com/hkstwk/calculation-module/compare/v1.2.4...v1.3.0) (2025-03-18)


### Features

* moved cors config to configuration class, added option to choose monthly deposit at start or end of period. ([2b5616e](https://github.com/hkstwk/calculation-module/commit/2b5616e1a566d6be33998b46624838fd33354eb6))

## [1.2.4](https://github.com/hkstwk/calculation-module/compare/v1.2.3...v1.2.4) (2025-03-09)


### Bug Fixes

* use secret iso GITHUB_TOKEN ([f5c4a41](https://github.com/hkstwk/calculation-module/commit/f5c4a419ffc17d9ea476b7b55f3dc60edcea80f4))

## [1.2.3](https://github.com/hkstwk/calculation-module/compare/v1.2.2...v1.2.3) (2025-03-06)


### Bug Fixes

* dummy change to test docker workflow ([93fb789](https://github.com/hkstwk/calculation-module/commit/93fb7890582bf5b7219b2038dafee7e2ac3d904a))

## [1.2.2](https://github.com/hkstwk/calculation-module/compare/v1.2.1...v1.2.2) (2025-03-06)


### Bug Fixes

* added unit tests for CompoundInterestCalculator.java. Added config for logback to enable debug logging in pure unit tests. ([7ed4c52](https://github.com/hkstwk/calculation-module/commit/7ed4c52e6e1efd14a775e72b39098dbd3c5f7ab4))

## [1.2.1](https://github.com/hkstwk/calculation-module/compare/v1.2.0...v1.2.1) (2025-03-02)


### Bug Fixes

* added some debug logging to CompoundInterestCalculator.java ([43a8b97](https://github.com/hkstwk/calculation-module/commit/43a8b9774463b9e5502594440632d1cf0c50f4e6))

# [1.2.0](https://github.com/hkstwk/calculation-module/compare/v1.1.4...v1.2.0) (2025-03-01)


### Features

* refactor to use dedicated calculation utility class. ([eb6b9c0](https://github.com/hkstwk/calculation-module/commit/eb6b9c051a62746f2b416c696c4187a013446765))

## [1.1.4](https://github.com/hkstwk/calculation-module/compare/v1.1.3...v1.1.4) (2025-03-01)


### Bug Fixes

* **deps:** update all maven dependencies ([dc866cc](https://github.com/hkstwk/calculation-module/commit/dc866ccfae9fb8b24b676173dbf6dffb32303091))

## [1.1.1](https://github.com/hkstwk/calculation-module/compare/v1.1.0...v1.1.1) (2025-02-02)


### Bug Fixes

* **compound interest:** add test coverage for FV calculation with monthly deposits; only include details in response when non null ([63dde66](https://github.com/hkstwk/calculation-module/commit/63dde66fddbc20c78494295d929f2448550cb466))

# [1.1.0](https://github.com/hkstwk/calculation-module/compare/v1.0.0...v1.1.0) (2025-02-01)


### Features

* **compound-interest:** add monthly deposit to calculation, excluding specific testcases ([571ba83](https://github.com/hkstwk/calculation-module/commit/571ba83899e2e0fe83a9720c33c57dd202ce9321))

# 1.0.0 (2025-01-30)


### Features

* initial release ([4d8bf4d](https://github.com/hkstwk/calculation-module/commit/4d8bf4d28e317cfa33b2e2aa927eb98e6bedb382))
