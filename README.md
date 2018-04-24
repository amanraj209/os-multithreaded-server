# OS Multithreaded Web Server
In this project repository, a multithreaded web server is implemented in Java to handle concurrent user requests.

This web server's performance to handle multiple requests is evaluated by comparing several scheduling algorithms used
in Operating Systems.

Scheduling algorithms are tested on this web server are:
- First Come First Serve Scheduling
- Shortest Job First Priority Scheduling
- Round Robin Scheduling

## How to run
- `git clone https://github.com/amanraj209/os-multithreaded-server.git`.
- Install Maven.
- Run WebServer.java using terminal or any IDE.
- To install maven dependencies, run `mvn install`.
- To load test the server with 1000 users concurrently, run `mvn verify`.
