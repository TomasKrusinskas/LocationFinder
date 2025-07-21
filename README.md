LocationFinder
A Scala 3 application for matching point locations to polygonal regions with JSON output.
Features

Point-in-polygon matching for geographic data
JSON input and output format
Fast geometric calculations
Comprehensive unit test suite
Built with Scala 3 and Scala CLI

Prerequisites

Scala CLI - For Scala 3 support and dependency management
Git - To clone the repository

Project Structure
LocationFinder/
├── build.sc                    # Scala CLI build configuration
├── src/                        # Application source files
│   ├── GeoMatcherApp.scala     # Main application entry point
│   ├── models/
│   │   ├── Location.scala      # Location data model
│   │   ├── Region.scala        # Region data model
│   │   └── RegionMatch.scala   # Match result model
│   └── utils/
│       ├── FileUtils.scala     # File I/O utilities
│       ├── GeoUtils.scala      # Geometry calculations
│       ├── RegionMatcher.scala # Core matching logic
│       └── Validator.scala     # Input validation
├── test/                       # Unit test suites (MUnit)
│   ├── GeoUtilsSuite.scala     # Geometry utilities tests
│   └── MatcherSuite.scala      # Matching algorithm tests
├── input/                      # Sample input JSON files
│   ├── regions.json           # Region definitions
│   └── locations.json         # Point locations
├── output/                     # Generated output
│   └── results.json           # Application results
└── README.md                   # This file
How to Run
From the project root directory, execute:
bashscala-cli run . -- --regions=input/regions.json --locations=input/locations.json --output=output/results.json
Command Options

run . - Compiles and runs all sources under src/
-- - Separates Scala CLI options from your program arguments
--regions - Path to the regions JSON file
--locations - Path to the locations JSON file
--output - Destination path for the results JSON file

After successful execution, inspect output/results.json for the matched results.
Running Tests
To execute the unit test suite using MUnit:
bashscala-cli test .
All test suites under test/ will run and validate the geometry logic and matching algorithm.
