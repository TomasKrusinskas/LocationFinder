# LocationFinder

Match point-locations to polygonal regions and output which locations fall into each region as JSON.

---

## Prerequisites

- **Scala CLI** (for Scala 3 support and dependency management)  
  [Get Scala CLI](https://scala-cli.virtuslab.org/)
- **Git** (to clone the repository)

---

## Project Structure

build.sc # Scala CLI build configuration (Scala version and dependencies)

src/
GeoMatcherApp.scala # Main app entry point

resources/
logback.xml # Logger config

models/
Location.scala
Region.scala
RegionMatch.scala

utils/
FileUtils.scala # Safe JSON file loading
GeoUtils.scala # Point-in-polygon logic
RegionMatcher.scala # Matching algorithm
Validator.scala # Input validation

test/
GeoUtilsSuite.scala # Point-in-polygon tests
MatcherSuite.scala # Region-location matching tests
JsonRoundTripSuite.scala # JSON (de)serialization tests

input/
regions.json
locations.json

output/
results.json

---

## How to Run

### 1. Run with Input Files

From the project root, run:

```sh
scala-cli run . -- \
  --regions=input/regions.json \
  --locations=input/locations.json \
  --output=output/results.json
run . tells Scala CLI to compile and run the app from src/.

-- separates Scala CLI flags from your app’s flags.

--regions: path to regions JSON

--locations: path to locations JSON

--output: path to write results JSON

Results will be written to output/results.json.

2. Run with Built-in Demo Data
To quickly try the app with built-in example data, run:


scala-cli run . -- --demo
This uses a sample region (a square) and three locations (inside, on the edge, and outside).

By default, results print to console.

To write demo results to a file:

scala-cli run . -- --demo --output=output/demo-results.json
Output Format
Results are a JSON array like:

[
  {
    "region": "DemoBox",
    "matched_locations": [
      "Inside",
      "OnEdge"
    ]
  }
]
Running Tests
To run all tests (point-in-polygon, region-matching, JSON round-trip):

scala-cli test .
