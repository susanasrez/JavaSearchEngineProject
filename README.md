<h1 align="center">LiBook: Book Search Engine 🔍 </h1>

  <img src="https://github.com/susanasrez/JavaSearchEngineProject/blob/master/logo.png" width="150" align="left" />
  <p>
    In this repository, you can find the source code for building up an inverted index based search-engine for books directly obtained from Project Gutenberg. We also implemented both relational and non-relational datamarts in order to be able to make queries to the index, which will be useful in the next phase of this Search Engine Project, and will give real functionality to the project. This project aims to provide a powerful search engine for books, making it easy for users to search for and access a wide range of literary works from Project Gutenberg's collection.
  
  </p>


<br>
<h2>1) <b>How to run</b> (Docker and Docker Compose)</h2>

For each module you should generate the corresponding docker image. If we take the indexer as a reference, a command like the following should be executed

```
docker build -t ricardocardn/indexer path_to_repo/Indexer/.
```

Or whether pull our own image directly

```
docker run -p 8081:8081 --network host ricardocardn/indexer
```

(*) The specification of the option ```--network host``` is crucial, and some problems related to hazelcast could raise if omitted. The query-engine image itself could be obtained in the following way

```
docker run -p 8080:8080 --network host susanasrez/queryengine
```

Other modules, Crawler and CLeaner, are already running on the server which ip is specified on the dockerfiles among the project, but could be refactored to execute it locally. If so, take a look at the docker compose file, and make sure that both modules are running in the same computer. Make also sure that active mq is running before starting the app.

<br>
<h2>Credits</h2>


- [Adam Brez](https://github.com/breznada/)
- [Susana Suárez](https://github.com/susanasrez)
- [Mara Pareja](https://github.com/marapareja17)
- [Joaquín](https://github.com/JoaquinIP)
- [Ricardo Cárdnes](https://github.com/ricardocardn)
