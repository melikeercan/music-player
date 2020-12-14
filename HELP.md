# Getting Started

### Dependencies:
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.0/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.0/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-developing-web-applications)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)
* [Spring Data Neo4j](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-neo4j)
* [Spring Data JDBC](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)
* [Accessing Data with Neo4j](https://spring.io/guides/gs/accessing-data-neo4j/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

Install and run Neo4j on docker. 
If you need to use different ports, update application properties.

Use the link to access the database
http://localhost:7474/browser/

Not required, but you may set initial data with the following queries:


	CREATE (a: Artist { id: '6cd8ff80-a52c-4e58-8da3-6722a8231713', artistName: 'Artist_1'});
	CREATE (a: Artist { id: 'f0e28b9a-9b63-4c14-8ac3-3b3cfe527883', artistName: 'Artist_2'});
	CREATE (a: Artist { id: '287a7739-579f-446c-9aa5-72a3524c56dc', artistName: 'Artist_3'});
	CREATE (a: Artist { id: '98783966-913a-4aa9-8614-47acc4433d5e', artistName: 'Artist_4'});

	CREATE (t: Track { id: '6f319b37-f8cc-48dc-b3c0-06b224c270c7', title: 'Track_1', durationInSeconds: 354 });
	CREATE (t: Track { id: 'fca31e65-823c-488b-a2a8-eb15fd2196be', title: 'Track_2', durationInSeconds: 300 });
	CREATE (t: Track { id: '291f6cd4-19b8-456c-b218-a6a7a0fed70f', title: 'Track_3', durationInSeconds: 220 });
	CREATE (t: Track { id: '5d031388-bf09-4379-97d9-e896a2ee2c6c', title: 'Track_4', durationInSeconds: 123 });
	CREATE (t: Track { id: '2a018692-25a6-4b7c-bfe2-7ebc7faa84f7', title: 'Track_5', durationInSeconds: 223 });
	CREATE (t: Track { id: '1e9b9a5b-6b86-4989-baac-6b3f238acf1d', title: 'Track_6', durationInSeconds: 300 });
	CREATE (t: Track { id: 'd8df697c-648c-49c1-8084-44d9b668b363', title: 'Track_7', durationInSeconds: 302 });
	CREATE (t: Track { id: '0f58ee0e-49b2-42b7-bfa9-ca8337f1d884', title: 'Track_8', durationInSeconds: 353 });
	CREATE (t: Track { id: 'c4901af3-5c77-4a63-a7e5-a50081f4dec7', title: 'Track_9', durationInSeconds: 253 });
	CREATE (t: Track { id: '77c641c3-024b-47a8-9076-5e417adff72b', title: 'Track_10', durationInSeconds: 153 });

	CREATE (p: Playlist { id: '85f6a2e3-c8a4-40c6-a4d4-f3008f9a5bc1', playListName: 'Playlist_1', registeredDate: 1591521148000, lastUpdated: 1591521148000, deleted: false, trackSize: 3 });
	CREATE (p: Playlist { id: '3bc6a34a-87d9-4e12-9dce-04b71880983a', playListName: 'Playlist_2', registeredDate: 1591521148000, lastUpdated: 1591521148000, deleted: false, trackSize: 2 });
	CREATE (p: Playlist { id: '472ad87c-67d2-4fb3-8267-92c677305476', playListName: 'Playlist_3', registeredDate: 1591521148000, lastUpdated: 1591521148000, deleted: false, trackSize: 4 });
	CREATE (p: Playlist { id: 'a7f42089-610c-4ecd-aabb-6fec75c5df83', playListName: 'Playlist_4', registeredDate: 1591521148000, lastUpdated: 1591521148000, deleted: true, trackSize: 0 });

	MATCH (t: Track { id: '6f319b37-f8cc-48dc-b3c0-06b224c270c7' }), (a: Artist { id: '6cd8ff80-a52c-4e58-8da3-6722a8231713' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_1', releaseYear: 2000}] -> (a);
	MATCH (t: Track { id: 'fca31e65-823c-488b-a2a8-eb15fd2196be' }), (a: Artist { id: '6cd8ff80-a52c-4e58-8da3-6722a8231713' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_1', releaseYear: 2000}] -> (a);
	MATCH (t: Track { id: '291f6cd4-19b8-456c-b218-a6a7a0fed70f' }), (a: Artist { id: '6cd8ff80-a52c-4e58-8da3-6722a8231713' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_2', releaseYear: 2010}] -> (a);
	MATCH (t: Track { id: '5d031388-bf09-4379-97d9-e896a2ee2c6c' }), (a: Artist { id: 'f0e28b9a-9b63-4c14-8ac3-3b3cfe527883' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_3', releaseYear: 2005}] -> (a);
	MATCH (t: Track { id: '2a018692-25a6-4b7c-bfe2-7ebc7faa84f7' }), (a: Artist { id: 'f0e28b9a-9b63-4c14-8ac3-3b3cfe527883' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_3', releaseYear: 2005}] -> (a);
	MATCH (t: Track { id: '1e9b9a5b-6b86-4989-baac-6b3f238acf1d' }), (a: Artist { id: '287a7739-579f-446c-9aa5-72a3524c56dc' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_4', releaseYear: 2004}] -> (a);
	MATCH (t: Track { id: 'd8df697c-648c-49c1-8084-44d9b668b363' }), (a: Artist { id: '287a7739-579f-446c-9aa5-72a3524c56dc' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_5', releaseYear: 2005}] -> (a);
	MATCH (t: Track { id: '0f58ee0e-49b2-42b7-bfa9-ca8337f1d884' }), (a: Artist { id: '287a7739-579f-446c-9aa5-72a3524c56dc' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_5', releaseYear: 2005}] -> (a);
	MATCH (t: Track { id: 'c4901af3-5c77-4a63-a7e5-a50081f4dec7' }), (a: Artist { id: '287a7739-579f-446c-9aa5-72a3524c56dc' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_6', releaseYear: 2006}] -> (a);
	MATCH (t: Track { id: '77c641c3-024b-47a8-9076-5e417adff72b' }), (a: Artist { id: '98783966-913a-4aa9-8614-47acc4433d5e' }) CREATE (t) - [:BELONGS_TO_ARTIST {albumName: 'Album_7', releaseYear: 2007}] -> (a);

	MATCH (t: Track { id: '6f319b37-f8cc-48dc-b3c0-06b224c270c7' }), (p: Playlist { id: '85f6a2e3-c8a4-40c6-a4d4-f3008f9a5bc1' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 0}] -> (t);
	MATCH (t: Track { id: 'fca31e65-823c-488b-a2a8-eb15fd2196be' }), (p: Playlist { id: '85f6a2e3-c8a4-40c6-a4d4-f3008f9a5bc1' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 1}] -> (t);
	MATCH (t: Track { id: '77c641c3-024b-47a8-9076-5e417adff72b' }), (p: Playlist { id: '85f6a2e3-c8a4-40c6-a4d4-f3008f9a5bc1' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 2}] -> (t);

	MATCH (t: Track { id: '5d031388-bf09-4379-97d9-e896a2ee2c6c' }), (p: Playlist { id: '3bc6a34a-87d9-4e12-9dce-04b71880983a' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 0}] -> (t);
	MATCH (t: Track { id: '6f319b37-f8cc-48dc-b3c0-06b224c270c7' }), (p: Playlist { id: '3bc6a34a-87d9-4e12-9dce-04b71880983a' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 1}] -> (t);

	MATCH (t: Track { id: 'c4901af3-5c77-4a63-a7e5-a50081f4dec7' }), (p: Playlist { id: '472ad87c-67d2-4fb3-8267-92c677305476' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 0}] -> (t);
	MATCH (t: Track { id: '77c641c3-024b-47a8-9076-5e417adff72b' }), (p: Playlist { id: '472ad87c-67d2-4fb3-8267-92c677305476' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 1}] -> (t);
	MATCH (t: Track { id: '0f58ee0e-49b2-42b7-bfa9-ca8337f1d884' }), (p: Playlist { id: '472ad87c-67d2-4fb3-8267-92c677305476' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 2}] -> (t);
	MATCH (t: Track { id: '291f6cd4-19b8-456c-b218-a6a7a0fed70f' }), (p: Playlist { id: '472ad87c-67d2-4fb3-8267-92c677305476' }) CREATE (p) - [:HAS_TRACK {dateAdded: 1591521148000, index: 3}] -> (t);
