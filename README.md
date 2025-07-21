# LocationFinder

**Match point‑locations to polygonal regions and output which locations fall into each region as JSON.**

---

## Prerequisites

- **Scala CLI** (for Scala 3 support and dependency management)  
- **Git** (to clone the repository)

---

## Project Structure

LocationFinder/
├── build.sc # Scala CLI build configuration
├── src/ # Application source files
│ ├── GeoMatcherApp.scala
│ ├── models/
│ │ ├── Location.scala
│ │ ├── Region.scala
│ │ └── RegionMatch.scala
│ └── utils/
│ ├── FileUtils.scala
│ ├── GeoUtils.scala
│ ├── RegionMatcher.scala
│ └── Validator.scala
├── test/ # Unit test suites (MUnit)
│ ├── GeoUtilsSuite.scala
│ └── MatcherSuite.scala
├── input/ # Sample input JSON files
│ ├── regions.json
│ └── locations.json
├── output/ # Generated output
│ └── results.json
└── README.md # This file

yaml

---

## How to Run

From the project root, run:

```bash
scala-cli run . -- \
  --regions=input/regions.json \
  --locations=input/locations.json \
  --output=output/results.json
run . tells Scala CLI to compile & run everything under src/.

-- separates Scala CLI flags from your app’s flags.

--regions: path to regions JSON

--locations: path to locations JSON

--output: where to write the results JSON

After running, check output/results.json for your matches.

Running Tests
bash
scala-cli test .
This runs all MUnit suites under test/.
