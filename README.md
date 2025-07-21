# LocationFinder

**Match point‑locations to polygonal regions and output which locations fall into each region as JSON.**

---

## Prerequisites

- **Scala CLI** (for Scala 3 support and dependency management)  
- **Git** (to clone the repository)

---

## Project Structure

build.sc
Scala CLI build configuration (Scala version and dependencies).

src/
– GeoMatcherApp.scala: Main application entry point.
– models/
  • Location.scala
  • Region.scala
  • RegionMatch.scala
(case‑classes and uPickle serializers)
– utils/
  • FileUtils.scala (safe JSON file loading)
  • GeoUtils.scala (point‑in‑polygon logic)
  • RegionMatcher.scala (matching algorithm)
  • Validator.scala (input validation)

test/
– GeoUtilsSuite.scala (unit tests for the geometry logic)
– MatcherSuite.scala (unit tests for the matching algorithm)

input/
– regions.json (sample region polygons)
– locations.json (sample point locations)

output/
– results.json (generated output showing which locations fall in which regions)

README.md
This file, with instructions for running and testing the app.

---

## How to Run

From the project root, run:

```
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
scala-cli test .
This runs all MUnit suites under test/.
