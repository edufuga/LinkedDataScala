# Run 'sbt stage' first.
./target/universal/stage/bin/main src/main/resources/products.csv src/main/resources/services.csv src/main/resources/orgmap.xml organisation.rdf | tee src/main/resources/output.txt
