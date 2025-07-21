LocationFinder

Match point-locations to polygonal regions and output which locations fall into each region as JSON.

Prerequisites

Scala CLI (for Scala 3 support and dependency management)

Git (to clone the repository)

Project Structure

The layout of this repository is shown below:

LocationFinder/
├── build.sc               # Scala CLI build configuration
├── src/                   # Application source files
│   ├── GeoMatcherApp.scala
│   ├── models/
│   │   ├── Location.scala
│   │   ├── Region.scala
│   │   └── RegionMatch.scala
│   └── utils/
│       ├── FileUtils.scala
│       ├── GeoUtils.scala
│       ├── RegionMatcher.scala
│       └── Validator.scala
├── test/                  # Unit test suites (MUnit)
│   ├── GeoUtilsSuite.scala
│   └── MatcherSuite.scala
├── input/                 # Sample input JSON files
│   ├── regions.json
│   └── locations.json
├── output/                # Generated output
│   └── results.json       # Created by the application
└── README.md              # This file

How to Run

From the project root directory, execute:

scala-cli run . -- \
  --regions=input/regions.json \
  --locations=input/locations.json \
  --output=output/results.json

run . compiles and runs all sources under src/.

The -- separates Scala CLI options from your program arguments.

--regions: path to the regions JSON file.

--locations: path to the locations JSON file.

--output: destination path for the results JSON file.

After successful execution, inspect output/results.json for the matched results.

Running Tests

To execute unit tests (using MUnit), run:

scala-cli test .

All test suites under test/ will run and validate the geometry logic and matching algorithm.

Notes

Regions can contain multiple polygons.

Points on polygon edges are handled by the ray-casting algorithm (may be considered inside).

The application includes validation for malformed JSON and coordinate formats.
