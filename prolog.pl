:- dynamic likes/2.
:- dynamic client/8.
:- dynamic taxi/7.
:- dynamic driver_speaks/2.

/*
 * client(X, Y, X_dest, Y_dest, time, persons, language, luggage)
 *
 * taxi(X, Y, id, available, capacity, rating, long_distance, type) 
 *
 * driverSpeaks(id, language)
 *
 * node(X, Y, line_id, name, file_line)
 *
 * lineSpecs(lineID, highway, name, oneway, lit, lanes, maxspeed,
 *    railway, boundary, access, natural, barrier, tunnel, bridge,
 *    incline, waterway, busway, toll)
 */

/* Holds true if line specified by LineID is not a railway, boundary, access prohibited,
 * natural, barrier, waterway.
 */ 

permittedWayType(living_street).
permittedWayType(motorway).
permittedWayType(motorway_link).
permittedWayType(primary).
permittedWayType(primary_link).
permittedWayType(secondary).
permittedWayType(secondary_link).
permittedWayType(tertiary).
permittedWayType(tertiary_link).
permittedWayType(residential).
permittedWayType(trunk).
permittedWayType(trunk_link).
permittedWayType(unknown).
permittedWayType(unclassified).

/* The following predicates hold true if the direction specified is allowed */

/* Holds true if two way line */
allowedDirection(LineID, _, _) :- 
    lineSpecs(LineID, Type, _, OneWayValue, _, _, _, no, no, no, no, no, _, _, _, no, _, _),
    permittedWayType(Type),
    OneWayValue = no.

/* If oneway line, determine if movement is allowed using the file line values (FLa, FLb) */
allowedDirection(LineID, FLa, FLb) :- 
    lineSpecs(LineID, Type, _, OneWayValue, _, _, _, no, no, no, no, no, _, _, _, no, _, _),
    permittedWayType(Type),
    OneWayValue = yes, 
    FLa < FLb.

allowedDirection(LineID, FLa, FLb) :- 
    lineSpecs(LineID, Type, _, OneWayValue, _, _, _, no, no, no, no, no, _, _, _, no, _, _),
    OneWayValue = -1, 
    permittedWayType(Type),
    FLa > FLb.

canMoveFromTo(Ax, Ay, Bx, By) :-
    node(Ax, Ay, LineID, _, FLa),
    node(Bx, By, LineID, _, FLb),
    allowedDirection(LineID, FLa, FLb).

score(living_street, 0.9).

score(residential, 0.65).

score(tertiary, 0.6).
score(tertiary_link, 0.6).

score(secondary, 0.5).
score(secondary_link, 0.5).

score(primary, 0.45).
score(primary_link, 0.45).

score(trunk, 0.4).
score(trunk_link, 0.4).
score(motorway, 0.4).
score(motorway_link, 0.4).

score(_, 0.7).

/* Rank lines */ 
highwayType(LineID, Type) :-
    lineSpecs(LineID, Type, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _). 

highwayRank(LineID, Value) :- 
    highwayType(LineID, Type), 
    score(Type, Score),
    Value = Score.

requestTime(Time) :-
    client(_, _, _, _, Time, _, _, _).

% The smaller the value, the better the traffic.
trafficValueToRank(high, 1).
trafficValueToRank(medium, 0.8).
trafficValueToRank(low, 0.6).

trafficRank(LineID, Rank) :-
    requestTime(Time),
    lineTraffic(LineID, StartTime, EndTime, Value),
    StartTime =< Time,
    EndTime >= Time,
    trafficValueToRank(Value, Rank).

% Traffic Rank for unspecified roads equals 1
trafficRank(_, 0.8).

numberOfLanes(LineID, Number) :-
    lineSpecs(LineID, _, _, _, _, Number, _, _, _, _, _, _, _, _, _, _, _, _). 

laneRank(LineID, LRank) :-
    numberOfLanes(LineID, Number),
    LRank is 1 - Number * 0.05.

% Define night times
isNight(Time) :-
    Time >= 2000, 
    Time =< 2359.

isNight(Time) :-
    Time >= 0000, 
    Time =< 0600.

% Result = yes if LineId has lights, otherwise no.
hasLights(LineID, Result) :-
    lineSpecs(LineID, _, _, _, Result, _, _, _, _, _, _, _, _, _, _, _, _, _). 

lightRank(LineID, LitRank) :-
    requestTime(Time),
    isNight(Time), 
    hasLights(LineID, yes),
    LitRank = 0.8.

% Default lightRank factor is 1
lightRank(_, 1).

/* Determine the coefficient of the road A==B. The less the coefficient the better the line. */
weightFactor(Ax, Ay, Bx, By, Value) :-
    node(Ax, Ay, LineID, _, _),
    node(Bx, By, LineID, _, _),
    highwayRank(LineID, HRank),
    trafficRank(LineID, TRank),
    laneRank(LineID, LRank),
    lightRank(LineID, LitRank),
    Value is HRank * TRank * LRank * LitRank.

/* Client Predicates */
speaksClient(Language) :-
    client(_, _, _, _, _, _, Language, _).

clientPassengers(Number) :-
    client(_, _, _, _, _, Number, _, _).

clientLuggage(Number) :-
    client(_, _, _, _, _, _, _, Number).

availableDriver(DriverID) :-
    taxi(_, _, DriverID, yes, _, _, _).

/* Driver Predicates */
driverRating(DriverID, Rating) :-
    taxi(_, _, DriverID, _, Rating, _, _).

drivesLongDistance(DriverID) :-
    taxi(_, _, DriverID, _, _, yes, _).

vehicleType(DriverID, Type) :- 
    taxi(_, _, DriverID, _, _, _, Type).

luggageFitsInVehicle(0, _).

luggageFitsInVehicle(NumberOfLuggage, DriverID) :-
    NumberOfLuggage > 0,
    vehicleType(DriverID, Type),
    Type \= compact,
    Type \= subcompact.

isQualifiedDriverForClient(DriverID) :-
    speaksClient(Language),
    speaksDriver(DriverID, Language),
    availableDriver(DriverID),
    clientPassengers(Number),
    maxPassengers(DriverID, Max),
    minPassengers(DriverID, Min),
    Number =< Max,
    Number >= Min,
    clientLuggage(NumberOfLuggage),
    luggageFitsInVehicle(NumberOfLuggage, DriverID).

driverRank(DriverID, Rank) :-
    taxi(_, _, DriverID, _, Rank, _, _).
