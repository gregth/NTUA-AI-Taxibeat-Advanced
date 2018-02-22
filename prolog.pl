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

/* Rank lines */ 
highwayType(LineID, Type) :-
    lineSpecs(LineID, Type, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _). 

highwayRank(LineID, 0.5) :- 
    highwayType(LineID, primary).

highwayRank(LineID, 1) :- 
    highwayType(LineID, tertiary).
    
highwayRank(LineID, 0.7) :- 
    highwayType(LineID, primary).


/* Determine the coefficient of the road A==B. The less the coefficient the better the line. */
coefficient(Ax, Ay, Bx, By, Value) :-
    node(Ax, Ay, LineID, _, _),
    node(Bx, By, LineID, _, _),
    highwayRank(LineID, HRank ),
    TRank is 1,
    Value is HRank * TRank.

/* A is the current Node, B is the examined child Node */
heuristsic(Ax, Ay, Bx, By, DistanceFromBToTarget, Value) :-
    coefficient(Ax, Ay, Bx, By, CoefficientValue),
    Value is CoefficientValue * DistanceFromBToTarget.
