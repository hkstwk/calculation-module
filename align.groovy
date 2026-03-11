def file = new File("readme.md")
def lines = file.readLines()

def result = []
def i = 0

while (i < lines.size()) {

    // Detect start of a table
    if (!lines[i].contains('|')) {
        result << lines[i]
        i++
        continue
    }

    // Collect continuous table lines
    def table = []
    while (i < lines.size() && lines[i].contains('|')) {
        table << lines[i]
        i++
    }

    // Parse rows (strip empty leading/trailing cells)
    def rows = table.collect { line ->
        line.split(/\|/).collect { it.trim() }.findAll { it }
    }

    // Compute max width per column
    def colCount = rows.collect { it.size() }.max()
    def colWidths = (0..<colCount).collect { col ->
        rows.collect { row -> row[col] ?: "" }
                .collect { it.length() }
                .max()
    }

    // Rebuild aligned table
    def aligned = rows.collect { row ->
        "| " + (0..<colCount).collect { col ->
            (row[col] ?: "").padRight(colWidths[col])
        }.join(" | ") + " |"
    }

    // Add formatted table to result
    result.addAll(aligned)
}

file.text = result.join("\n")

println "All tables formatted and written back to file."
