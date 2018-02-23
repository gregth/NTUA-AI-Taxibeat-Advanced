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

permittedWayType(highway).
permittedWayType(living_street).
permittedWayType(motorway).
permittedWayType(motorway_link).
permittedWayType(primary).
permittedWayType(primary_link).
permittedWayType(residential).
permittedWayType(secondary).
permittedWayType(secondary_link).
permittedWayType(tertiary).
permittedWayType(trunk).
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

score(living_street, 0.6).
score(secondary, 0.4).
score(secondary_link, 0.4).
score(primary, 0.5).
score(primary_link, 0.5).
score(tertiary, 0.5).
score(trunk, 0.5).
score(motorway, 0.5).
score(motorway_link, 0.5).
score(_, 0.8).

/* Rank lines */ 
highwayType(LineID, Type) :-
    lineSpecs(LineID, Type, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _). 

highwayRank(LineID, Value) :- 
    highwayType(LineID, Type), 
    score(Type, Score),
    Value = Score.

/* Determine the coefficient of the road A==B. The less the coefficient the better the line. */
weightFactor(Ax, Ay, Bx, By, Value) :-
    node(Ax, Ay, LineID, _, _),
    node(Bx, By, LineID, _, _),
    highwayRank(LineID, HRank),
    Value is HRank.

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

luggageFisInVehicle(0, _).

luggageFisInVehicle(NumberOfLuggage, DriverID) :-
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
    luggageFisInVehicle(NumberOfLuggage, DriverID).

driverRank(Rank) :-
    taxi(_, _, DriverID, _, Rank, _, _).
