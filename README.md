![CI](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin/workflows/CI/badge.svg)

Spinnaker Plugin (PF4J-based) that is a custom pipeline stage. 
The [pf4jStagePlugin](https://github.com/spinnaker-plugin-examples/pf4jStagePlugin) creates a custom pipeline stage that waits a specified number of seconds before signaling success. This plugin implements the [SimpleStage](https://github.com/spinnaker/orca/blob/ab89a0d7f847205ccd62e70f8a714040a8621ee7/orca-api/src/main/java/com/netflix/spinnaker/orca/api/SimpleStage.java) PF4J extension point in Orca. The plugin consists of a `random-weight-orca` [Kotlin](https://kotlinlang.org/docs/reference/) server component and a `random-wait-deck` [React](https://reactjs.org/) UI component.

This is for demo only and not meant to be used in a production environment.

# Dependencies

[Gradle](https://gradle.org/) and [Yarn](https://classic.yarnpkg.com/en/) for building the plugin locally
 
| Plugin Version  | Orca Version |
|:----------- | :--------- |
| 1.0.17  |  Orca master (8 April 2020) |
| 1.1.x  | Orca master (8 April 2020) | 

# Usage

1) Run `./gradlew releaseBundle`
2) Put the `/build/distributions/<project>-<version>.zip` into the [configured plugins location for your service](https://pf4j.org/doc/packaging.html).
3) Configure the Spinnaker service. Put the following in the service yml to enable the plugin and configure the extension.:

```
spinnaker:
  extensibility:
    plugins:
      Armory.RandomWaitPlugin:
        enabled: true
        extensions:
          armory.randomWaitStage:
            enabled: true
            config:
              defaultMaxWaitTime: 60
```

Or use the [examplePluginRepository](https://github.com/spinnaker-plugin-examples/examplePluginRepository) to avoid copying the plugin `.zip` artifact.

# Debugging

To debug the `random-weight-orca`  server component inside a Spinnaker service (like Orca) using IntelliJ Idea follow these steps:

1) Run `./gradlew releaseBundle` in the plugin project.
2) Copy the generated `.plugin-ref` file under `build` in the plugin project submodule for the service to the `plugins` directory under root in the Spinnaker service that will use the plugin .
3) Link the plugin project to the service project in IntelliJ (from the service project use the `+` button in the Gradle tab and select the plugin build.gradle).
4) Configure the Spinnaker service the same way specified above.
5) Create a new IntelliJ run configuration for the service that has the VM option `-Dpf4j.mode=development` and does a `Build Project` before launch.
6) Debug away...

# Architecture


## random-weight-orca

The `random-weight-orca` backend consists of five classes in the `io.armory.plugin.state.wait.random` package:

* `Context.kt`: a data class that stores the `maxWaitTime` value; `SimpleStage` uses `Context`
* `Output.kt`: a data class that stores the `timeToWait` getValue; this data is returned to the extension point implementation and can be used in later stages
* `RandomWeightConfig.kt`: a data class with the `@ExtensionConfiguration` tag; key-value pairs in this class map to the plugin's configuration
* `RandomWeightInput.kt`: a data class that contains the key-values pairs that we care about from the Context map
* `RandomWaitPlugin.kt`: this is the plugin's main class; implements `SimpleStage`

[Video walkthrough](https://youtu.be/u9NVlG58NYo)

## random-weight-deck

This UI component is written in React and uses the [rollup.js](https://rollupjs.org/guide/en/#plugins-overview) plugin library.

* `rollup.config.js`:  plugin configuration
* `plugins.json`: defines dependencies
* `RandomWaitStage.tsx`: defines the custom pipeline stage; renders UI output
* `RandomWaitStageIndex.ts`: exports the name and custom stages

[Video walkthrough](https://youtu.be/u9NVlG58NYo)