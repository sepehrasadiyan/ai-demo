# Text Summarization Service

## Architecture
### High-Level Modules
- **`api`**: Controllers and exception handlers
- **`core`**: Ports, input/output records, provider IDs
- **`adapters`**: Provider implementations (OpenAI-compatible)
- **`core/decorators`**: User guard and provider-specific decorators
- **`core/select`**: Registry and selection policy
- **`core/preprocess`**: Text preprocessing pipeline
- **`user`**, **`history`**: MongoDB documents, repositories, services
- **`bootstrap`**: Admin seeding runner

## Tech Stack
- Java 21
- Spring Boot 3.4 (Web, Data MongoDB, Validation)
- MongoDB 7
- Jackson
- Lombok

## Getting Started
### Prerequisites
- Java 21
- Maven/Gradle
- Docker (for local MongoDB)


# You have to use your own API key in yml file
# You have to use your own Mongo address other thing works like a charm

## To test use below curl

curl --location 'http://localhost:8080/api/v1/summaries' \
--header 'Content-Type: application/json' \
--header 'X-User-Id: 8e305566-a042-4a64-a493-b893c27b1ee0' \
--data '{
"text": "Record patterns let you deconstruct records directly in if, instanceof, and switch.\nPattern matching for switch works with records, enabling concise case arms that bind components.\nUnnamed variables and patterns let you ignore unneeded components using the underscore.\nNested record patterns support deep deconstruction of records within records without boilerplate.\nGuarded patterns allow attaching conditions to cases, improving readability and safety.\nPattern matching refinements broaden where records can be matched alongside other types.\nRecords pair naturally with sealed hierarchies to model algebraic data in a type safe way.\nExhaustiveness checks in switch encourage complete handling of shapes, reducing runtime errors.\nUsing records with pattern matching often removes getters, equals, and manual mapping code.\nTogether these features push Java toward a more declarative, data oriented style with predictable performance."
}
'
