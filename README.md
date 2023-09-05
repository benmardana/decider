# Decider

A Java library for reading and parsing If-statements encoded as JSON. 

## Requirements

- Java 17
- Gradle 8.3

## Usage

If the JDK and Gradle are installed, run `gradle test` to see usage examples.

Alternatively, if Docker is preferred, run `./batect test`.

See https://batect.dev for more info.

Log level can be adjusted via the `logLevel` field in the [CustomLogger](lib/src/main/java/decider/infra/CustomLogger.java) for more info.

## Notes

I've consciously tried to keep the application as simple as possible with as limited dependencies as possible.

### JSON Schema

A JSON schema can be found in the resources: [schema.json](lib/src/main/resources/schema.json)

A sample fulfilling the schema is found in the test resources: [sample.json](lib/src/test/resources/sample.json).

```json
{
  "branches": [
    {
      "conditions": [
        {
            "left": "%1$s",
            "operator": "==",
            "right": "abc"
        },
        {
          "left": "%2$s",
          "operator": ">",
          "right": 4
        }
      ],
      "consequent": true
    },
    {
      "condition": {
        "left": "%2$s",
        "operator": "<",
        "right": 10
      },
      "consequent": true
    }
  ],
  "alternative": false
}
```

This example is analogous to the following pseudocode:

```
if ($1 == "abc" && $2 > 4) {
  return true
}
if ($2 < 10) {
  return true
}
return false
```

An if statement is represented by a list of branches that may or not resolve along with an alternative if none of the branches resolve.

Branches can be single condition (e.g. `if(a === "abc")`) or collections of conditions (e.g. `if(a == "abc" && b > 4)`).

The program will iterate the branches one by one, evaluating the conditions.

If a branch is a single condition, then a truthy resolution will result in the corresponding `consequent` being returned.

If a branch is a collection of conditions, then all must be true to return the `consequent` value.

Conditions are made up of left and right operands, and an operator. The supported operations are as follows:
- &&
- ||
- ==
- !=
- <
- <=
- \>
-  \>=

Variables are supported via Java string interpolation. E.g. `"%1$s"` will be replaced by the first provided binding variable.

## Supported operations

The Decider class exposes the API of the library. The available operations are exercised in the [DeciderTest](lib/src/test/java/decider/DeciderTest.java).

Available operations:
- Read(path, bindings?, datatype?) -> String
  - Reads in a json file, optionally apply bindings, and output a string in the desired data format (JSON and YAML currently supported)
- Evaluate(path, bindings) -> Object
  - Reads in a json file, applies bindings and evaluates the statement, returning the consequent or alternative value
- Write(string, path)
  - Writes a string to a given path, used for demonstrating how a condition can be modified.