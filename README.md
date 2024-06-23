# Pet Friendly Boat Rental Application

This application provides a GraphQL API for creating and updating pet information in a pet-friendly boat rental context.
Also, it provides information about the eligibility of the pets for the boat rental.

## Database structure

1. PET_OWNER table:

```
id: Primary Key
government_id: Text
first_name: Text
last_name: Text
email: Text
phone_number: Text

```

2. PET table:
```
id: Primary Key
owner_id: Foreign Key referencing PET_OWNER.id
name: Text
weight: Double
breed: Text
training_level: Integer
is_vaccinated: Boolean
species: Text
```

The PET table has a foreign key (owner_id) that references the id of the PET_OWNER table, creating a one-to-many relationship from PET_OWNER to PET

![](/Users/daniel_aguirre1/test/petscreening-boat-rental-service-test/database.png)

## Running the application

### Pre-requisites

- Docker Compose
- JDK 17

### Start Postgres database

From the application root folder, run `docker-compose up -d`. 
Wait a few seconds for the flyway migration to finish (Running `docker ps` should only show 1 docker container running: `petscreening-boat-rental-service-test-postgres-1`, 
if you see `petscreening-boat-rental-service-test-flyway-1` it means the flyway migration hasn't finished yet.)

### Start application

From the application root folder, execute `mvn spring-boot:run` command to start the application

## Interacting with the GraphQL API

You can interact with the API using any HTTP client or a GraphQL client like GraphiQL.

Open the following URL in a browser to use GraphiQL: http://localhost:8080/graphiql?path=/graphql and click on Show GraphiQL Explorer (Third icon in the top left corner).
Then you can add the Query or Mutation that you want to use

The GraphQL endpoint is `/graphql`.

### Queries

#### Get Eligible Pets

To get a list of eligible pets based on certain criteria, use the `getEligiblePets` query. 
You can provide `isVaccinated`, `maxWeightInPounds`, `minimumTrainingLevel`, and `excludedBreed` as arguments. All of them are optional, so if you don't provide any of them, it will retrieve all the pets stored in the database.

```graphql
query {
    getEligiblePets(isVaccinated: true, maxWeightInPounds: 50, minimumTrainingLevel: 3, excludedBreed: "Breed1") {
        id
        name
        breed
        weight
        isVaccinated
        trainingLevel
        species
        owner {
            email
            firstName
            phoneNumber
            lastName
            id
            governmentId
        }
    }
}
```


### Check Pet Eligibility for Rental

To check if a pet is eligible for rental, use the isPetEligibleForRental query. You need to provide petId as an argument.
In case the pet is not eligible, it will provide a list of reasons why the 

```graphql
query {
    isPetEligibleForRental(petId: 1) {
    isEligibleForRental
    reasons
    }
}
```

### Mutations

#### Create Pet and Owner

To create a pet and its owner at the same time, use the `createPetAndOwner` mutation. You need to provide a `PetWithOwnerInput` object with the pet and owner details.

```graphql
mutation {
    createPetAndOwner(petWithOwner: { pet: { name: "Rex", species:DOG breed: "Labrador", weight: 20, vaccinated: true, trainingLevel: 3 }, owner: { governmentId: "123456789", firstName: "John", lastName: "Doe", email: "john.doe@example.com", phoneNumber: "123-456-7890" } }) {
        weight
        trainingLevel
        species
        owner {
            phoneNumber
            lastName
            id
            governmentId
            firstName
            email
        }
        name
        isVaccinated
        id
        breed
    
    }
}
```

#### Create Pet and Link Owner
To create a pet and link it to an existing owner, use the createPetAndLinkOwner mutation. You need to provide a PetWithOwnerIdInput object with the pet details and the owner's ID.

```graphql
mutation {
    createPetAndLinkOwner(petWithOwnerId: { pet: { name: "Bella" species: DOG breed: "Golden Retriever", weight: 25, vaccinated: true, trainingLevel: 2 }, ownerGovernmentId: "123456789" }) {
        id
        name
        species
        breed
        weight
        isVaccinated
        trainingLevel
        owner {
          id
          governmentId
          firstName
          lastName
          email
          phoneNumber
        }
    }
}
```


#### Update Pet

To update a pet, use the `updatePet` mutation. You need to provide the `PetInput` object with the updated details.
Only the id is mandatory, so you can update specific fields of the pet without the need to send all the unchanged fields.

```graphql
mutation {
    updatePet(petInput: { id: 1 name: "New Name", breed: "New Breed", weight: 20, vaccinated: true, trainingLevel: 3 species:DOG }) {
        id
        name
        breed
        weight
        isVaccinated
        trainingLevel
        species
    }
}
```

### Create Pet Owner
To create a pet owner, use the createPetOwner mutation. You need to provide an OwnerInput object with the owner details.

```graphql
mutation MyMutation {
  createPetOwner(
    ownerInput: {governmentId: "123456", email: "test@email.com", firstName: "Mike", lastName: "Smith", phoneNumber: "555555555"}
  ) {
    email
    phoneNumber
    lastName
    id
    governmentId
    firstName
  }
}
```

### Change Owner to Pet
To change the owner of a pet, use the changeOwnerToPet mutation. You need to provide the `petId` and the `ownerId` or `ownerGovernmentId`.

```graphql
mutation MyMutation {
  changeOwnerToPet(petId: "1", ownerId: "4") {
    breed
    id
    isVaccinated
    name
    species
    trainingLevel
    weight
    owner {
      email
      lastName
      id
      governmentId
      firstName
      phoneNumber
    }
  }
}
```

Each mutation will return the updated object.

You can also run the queries and mutations with Curl or import the Curl command to run it in postman. 

Query Example:

```shell
curl --location 'http://localhost:8080/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"query {  getEligiblePets {  name   species  breed   id  isVaccinated   trainingLevel    weight   owner {   email     firstName   phoneNumber   lastName   id governmentId } }}","variables":{}}'
```