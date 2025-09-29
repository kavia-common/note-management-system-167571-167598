# Notes App Backend (Spring Boot) - Ocean Professional Theme

A minimalist, modern Spring Boot backend that provides:
- User registration and authentication (demo token-based)
- CRUD operations for notes
- Clean Swagger UI with Ocean Professional theme
- H2 in-memory database

## Run
- ./gradlew bootRun
- Visit /swagger-ui.html

## Auth
- Register: POST /api/auth/register
- Login: POST /api/auth/login -> returns token
- Use Authorization: Bearer <token> for notes endpoints

## Notes
- List: GET /api/notes
- Create: POST /api/notes
- Get: GET /api/notes/{id}
- Update: PUT /api/notes/{id}
- Delete: DELETE /api/notes/{id}

## Sample
curl -sX POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"demo","email":"demo@example.com","password":"pass"}'
TOKEN=$(curl -sX POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"demo","password":"pass"}' | jq -r .token)
curl -sX POST http://localhost:8080/api/notes -H "Authorization: Bearer ${TOKEN}" -H "Content-Type: application/json" -d '{"title":"First","content":"Hello"}'
