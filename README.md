# What is this project?

This project demos how you can build a scheduler as a service
using [db-scheduler](https://github.com/kagkarlsson/db-scheduler).

## How to run

```
docker compose up -d
```

The above command will build the code, and bring up the server and its dependencies.

Navigate to http://localhost/swagger-ui/index.html

There are 2 APIs,

1. `POST /api/schedule` -> This schedules a task.
2. `DELETE /api/schedule/{id}` -> Deletes a scheduled task.
