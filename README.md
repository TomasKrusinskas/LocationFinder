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
  
– utils/

  • FileUtils.scala (safe JSON file loading)
  
  • GeoUtils.scala (point‑in‑polygon logic)
  
  • RegionMatcher.scala (matching algorithm)
  
  • Validator.scala (input validation)

test/

– GeoUtilsSuite.scala

– MatcherSuite.scala


input/

– regions.json

– locations.json


output/

– results.json

---

## How to Run

From the project root, run:

```
scala-cli run . -- \
  --regions=input/regions.json \
  --locations=input/locations.json \
  --output=output/results.json
run . tells Scala CLI to compile & run everything under src/.

-- separates Scala CLI flags from app’s flags.

--regions: path to regions JSON

--locations: path to locations JSON

--output: path to results JSON

After running, the results will be written to results.json.

Running Tests
scala-cli test .
This runs all MUnit suites under test/.
