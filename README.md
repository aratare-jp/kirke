> Currently not complete!

# Kirke

> A great enchantress famous for Her knowledge of herbs, magical spells and dark enchantments. She is especially
> known for spells of transformation: She changed the beautiful nymph Skylla into a sea-monster because She was jealous 
> of Her; Picus, who refused Her love, She turned into a woodpecker; and any mortal visitors to Her island were 
> transformed into wild beasts.

Kirke is a data transformation tool that allows multiplex transformation on multiple data sources.
In simpler terms:
> It takes data from multiple sources, does things to it, then places it somewhere else.

The transformation takes place during the "does thing to it" phase, which can comprise of many tasks such as:
- Conversion
- Aggregation
- Separation
- Etc.

## Tasks
At the lowest level in Kirke are tasks. A task performs a single operation on an input, which produces an output.

```
Input --> Operation --> Output
```

But it gets even better! A task can also have zero or _multiple_ inputs, each of which can come from different data 
sources, and zero or _multiple_ output, each can also be linked up to different places. 

A zero-input task is called a ***producer***, and an example of such task can be a data source that generates data on 
the fly.

A zero-output task is called a ***consumer***, and is typically used for IO-related tasks such as file writer.

By itself a task is not as useful, if not sometimes unwieldy as it grows overtime and has to perform a lot of
transformation steps. So instead, you can link up multiple tasks together to form a transformation graph that can move
data freely within the graph.

Here a visualisation of a transformation graph that has three tasks:
![](./images/tasks.png)

The flow starts when we read a file and ends when we write to a file. By having multiple tasks instead of one big task
to handle everything, we can start modularise our data flow. For example, if we want to fetch data from an URL we only 
need to create a producer to do the fetching, and then link it up with our Task 2.

> You may wonder why `Read meme` and `Read meme file` are not the same (one is an operation one is an input). You can
> check out further details in [Input](#input) and [Operations](#operation) sections. But TL;DR: Input treats 
> everything that it receives as pure data whereas a file is only a data store (not the data itself). Hence you need an 
> operation to "unbox" the data store and retrieve the data within it.

There are currently multiple built-in tasks that provide most common functionality, however you can also create your own
task which can give you more freedom over how you want to process your data. Check out [Custom tasks]() for more info.

Each task must be given a unique ID which can be referred to by other tasks. This is simply denoted with `:id` key with
a value of any string.

### Input
A task can receive input from zero or multiple data sources. Some of the most common data sources are databases, files,
servers, or even other tasks. 

Input receives data _only_, which is different from other frameworks that may treat input as both data and data stores.
Kirke only allows raw data to be received by input, whereas [operations](#operation) can be used to process the data
store itself. Operations are also used to data generation as "generation" does not mean the data but rather the act of 
generating itself. The rationale behind this decision is so that we can keep input as "pure" as possible and that input
should ever be concerned about the data itself.

Input can also be attached with [triggers](#trigger) which can perform various things upon receiving input data. One of
which is the well-known and much-needed data validation.

Input is denoted with the `:input` keyword whose value is another map with the following keys:

| Key | Value | Default value | Description |
| ---- | ---- | ---- | ---- |
| `:sources` | String or keyword vector | String vector | Can either be URLs to a file, a web endpoint, a database or a task ID (prefixed with `{graph-id}:`) |
| `:triggers` | String or keyword vector | String vector | Indicates what triggers to apply upon receiving input. Can either be built-in trigger or trigger ID (prefixed with `id:`) |

### Operation
Operation lays out what transformation to perform to the received input. Each task type varies depending on what type of
transformation it does. Some of the most commonly used tasks are:
- `Converter`
- `Logger`
- `Reader`
- `Writer`

As mentioned previously, operation can also handle any processing of the data store itself. For example, fetching data
from a web endpoint or reading from a file. This means operations can potentially introduce side-effects.

Operation is denoted with the `:operation` keyword whose value is another map with the following keys:

| Key | Value | Default value | Description |
| ---- | ---- | ---- | ---- |
| `:type` | String | String | Indicates the type of the operation or an operation ID (prefixed with `{graph-id}:`) |
| `:params` | String or keyword vector | String vector | Configuration applicable to the task itself. |

### Output
Once operation is performed, the output data is then sent to a data store or other tasks.

Triggers can also be attached to the output once transformation is completed.

Output is denoted with the `:output` keyword whose value is another map with the following keys:

| Key | Value | Default value | Description |
| ---- | ---- | ---- | ---- |
| `:targets` | String or keyword vector | String vector | Can either be URLs to a file, a web endpoint, a database or a task ID (prefixed with `{graph-id}:`) |
| `:triggers` | String or keyword vector | String vector | Indicates what triggers to apply upon receiving input. Can either be built-in trigger or trigger ID (prefixed with `id:`) |

### Trigger
Trigger allows intra- and inter-graph communication that is not possible with just task linking. Conceptually trigger is
an event that is broadcasted once a condition is met. In practice it is a way to logically link between different tasks
without explicitly saying so.

An event is sent that will be consumed by trigger, even a producer or a consumer! (For more info check out 
[Producer](#producer) and [Consumer](#consumer) sections).

A event is map with the following keys:

| Key | Value | Default value | Description |
| ---- | ---- | ---- | ---- |
| `:source` | String | String | ID of the task that sent the event (prefixed with `{graph-id}:` |
| `:payload` | Any | String | Whatever data to send to the trigger |

Triggers are mostly used for tasks that are not tightly coupled with the data flow and whose concern is spanned across
multiple graphs, such as logging or validating. It is called 
[Aspect-oriented programming](https://en.wikipedia.org/wiki/Aspect-oriented_programming) in OO world.

Triggers come in different flavours, but the most commonly used ones are:
- Logger
- Validator

You saw it right: Logger is a trigger _and_ a task. Which brings us to the next point:
> Trigger is simply a task that can also handle spontaneous execution.

Whereas task takes its input from other tasks or producers, trigger takes input from events, even the ones from other
graphs!

There are two types of trigger: synchronous and asynchronous.
- **_Synchronous_**: Upon firing an event, the firing task will _wait_ until the trigger has finished executing before 
resuming its execution.
- **_Asynchronous_**: Upon firing an event, the firing task will _not_ wait for the trigger to finish executing and will
continue its execution.

For example, logger is an **_async_** trigger because it should not block the task from executing its operation (though 
it can be configured to), whereas validator is an **_sync_** trigger because execution should not be continued if the 
data is invalid (though it can be configured to).

## Built-ins
Here is a list of all of the commonly used and built-in tasks and triggers that Kirke provides out-of-the-box.

### Conversion
This task reads data, does conversion, and then output the converted data. What type of conversion varies depending on
what type of input it receives and what possible conversion can be done to the input data.

## License

Copyright © 2019

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
