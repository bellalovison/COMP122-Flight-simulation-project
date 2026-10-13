# COMP122-Flight-simulation-project
Group project - Java console simulation of an airport flight board, with domestic and international zones, gate allocation, and live or stepped time-based arrivals and departures.
# FlightBoard

A Java console application that simulates an airport's arrivals and departures
board. Flights are imported from CSV files, assigned to gates in domestic or
international zones, and move through a realistic lifecycle while the board
updates in real time.

## Features

- Build an airport from one or more zones, each with its own number of gates
  and a domestic or international designation
- Import flight databases from CSV files (domestic and international)
- Random flight number generation, with no duplicates
- Automatic gate assignment, with delays when no suitable gate is free
- Flight status lifecycle: Scheduled → Arrived/Delayed → At Gate → Boarding →
  Final Call → Departed
- Two simulation modes:
  - **Live:** runs continuously with an adjustable time scale
    (real seconds per simulated hour)
  - **Recursive:** runs a set number of simulated minutes, then shows the board
- Console display of domestic departures, international departures, and arrivals

## Project structure

| File | Purpose |
|------|---------|
| `FlightBoardApp.java` | Entry point and user input handling |
| `Airport.java` | Manages zones and the arrival/departure boards |
| `Zone.java` / `Gate.java` | Terminal zones and individual gates |
| `Flight.java` | Flight data and status |
| `SimulationEngine.java` | CSV import, scheduling, and flight lifecycle logic |
| `TimeEngine.java` | Scaled simulation clock |

## CSV format

One flight per line, with the airline in the first column and the destination or
origin city in the third column:

    Air New Zealand,,Wellington
    Jetstar,,Christchurch

## How to run

From the folder that contains the `FlightBoard` directory:

    javac FlightBoard/*.java
    java FlightBoard.FlightBoardApp

When prompted for the CSV path, give it relative to where you ran the command
(e.g. `FlightBoard/auckland_domestic.csv`).

## Authors

Sarah AI-Shanfari and Bella Lovison
