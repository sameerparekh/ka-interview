# KA Interview

## Compile
	
	cd src/usergraph
	scalac *.scala

## Run

	scala usergraph.Test

## Visualization (requires graphviz)

	dot -Tpng -o TestNumber.png < TestNumber.dot

Infected users are filled in red, the root user of the infection is noted with a pentagon.

## Notes

A better design would be to put the graph description into a separate immutable class, rather
than embedding it with mutable HashSets into the User class, so that the only mutable piece of the
User object becomes the version. I located the scalax.collection.Graph SDK, but I have not yet
learned SBT and/or how to incorporate third-party Scala libraries, so for reasons of limited time I
left the graph itself embedded in the multiple User objects.







