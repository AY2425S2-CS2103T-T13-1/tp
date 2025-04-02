---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-T13-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/AY2425S2-CS2103T-T13-1/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-T13-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-T13-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-T13-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-T13-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Using Help within the application itself

The following activity diagram summarizes what happens when a user executes a help command:
![Help Command Activity Diagram](images/HelpCommandActivityDiagram.png)

The command makes use of the `/` prefix to check for the parameters provided. If the parameter is valid (meaning it is one of the other commands available in FitFlow), it will show the message usage of that command requested.

### Adding/Editing a Client

The following activity diagram summarizes the workflow of when a client is added or edited in FitFlow.
![Add_Edit Command Activity Diagram](images/AddCommand_EditCommandActivityDiagram.png)

The commands check for schedule conflicts between the client being added/edited and other existing clients stored in FitFlow and warns the user of any such conflicting timings.

### Using View within the application itself

The following activity diagram summarizes what happens when a user executes a view command:
![View Command Activity Diagram](images/ViewCommandActivityDiagram.png)

The command takes in a DAY/DATE parameter. If the parameter is valid, it will list the clients' schedules for the given day or date.

### Using Display within the application itself

The following activity diagram summarizes what happens when a user executes a display command:
![Display Command Activity Diagram](images/DisplayCommandActivityDiagram.png)

The command takes in a positive integer parameter. If the parameter is valid (meaning it is less than or equal to the number of clients), it will display the client's details at the index.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* a professional personal trainer in Singapore helping clients achieve fitness goals
* needs to track and manage individual client goals
* creates and edits customised training plans based on client needs
* keeps track of client details (name, phone number, training days, goals, medical history and location)
* manages weekly schedule and multiple training locations across Singapore
* organise group workout sessions with clients who have similar exercise profiles and live close together

**Value proposition**: streamlines client management, workout planning, and scheduling, maximizing productivity

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                          | I want to …​                                                                   | So that I can…​                                                                        |
|----------|----------------------------------|--------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| `* * *`  | user                             | add my clients' contact details and location to the app                         | I can contact them easily if there are any changes in plans                            |
| `* * *`  | user                             | add my clients' workout goals in the app                                       | I can easily plan workout routines for my clients                                      |
| `* * *`  | user                             | add my clients' previous or existing injuries in the app                       | I can better plan exercises that avoid aggrevating their injury                        |
| `* * *`  | user                             | add my clients' preferred location                                             | I can collate a list of clients in the same area to train                              |
| `* * *`  | user                             | sort my clients' information to see if they match with my schedule             | I can better plan the timing to train while I am free                                  |
| `* * *`  | new user                         | look at the onboarding/help section                                            | I know the features the app provides and how to use them                               |
| `* * *`  | new user                         | go through a guided tutorial from the application                              | learn the basic features of the app and test it out                                    |
| `* * *`  | user                             | view the schedule for each day                                                 | I can plan my day accordingly                                                          |
| `* * *`  | user                             | update each clients' details                                                   | I can make changes and keep things up to date with what happens IRL                    |
| `* * *`  | user                             | view client's preference on solo or group trainings                            | I can organise a joint training session to maximise productivity                       |
| `* *`    | user                             | add recurring events for clients that have training on the same day every week | I don't need to manually add and manage my schedule every single week                  |
| `* *`    | user                             | be notified of a conflicting timeslot when I update a client's timeslot        | I know which timeslots are not available for my new clients                            |
| `* *`    | user                             | delete client that I am not training anymore                                   | the client list I have is not messy                                                    |
| `* *`    | forgetful user                   | set reminders for training sessions                                            | I do not forget or miss a training session with my client                              |
| `* *`    | user                             | set goal deadlines for clients                                                 | I know which session to have a sit down with client                                    |
| `*`      | organized user                   | sort my clients chronologically                                                | I will know which clients I will be meeting with soon.                                 |
| `*`      | user                             | sort my clients based on months or weeks                                       | I can better plan for the clients that are in the upcoming months / weeks              |

### Use cases

(For all use cases below, the **System** is the `FitFlow` and the **Actor** is the `User`, unless specified otherwise)

---

**Use case 1: View Application Usage**

**MSS**

1. The new user requests for help from Fit Flow to see the functionality of the application.
2. FitFlow displays usage instructions for each function.
3. The new user reads the instructions.<br>
   Use case ends.

**Extensions**

- 2a. User requests to see the specific help page for a command (e.g. help add).
    - 2a1. FitFlow shows the User the specific command’s help text.<br>
      Use case resumes at step 3.

---

**Use case 2: Add Client**

**MSS**

1. User chooses to add a new client.
2. FitFlow stores the details of the client and indicates success.
3. The new client is added to the displayed client list.<br>
   Use case ends.

**Extensions**

- 1a. The client details are given in the wrong format.
    - 1a1. FitFlow shows the user the format the client’s details should be entered.
    - 1a2. User enters new data.<br>
      Steps 1a1-1a2 repeat until the data is entered.<br>
      Use case resumes at step 2.
- 1b. User provides a session timing that conflicts with an existing session timing.
    - 1b1. FitFlow shows the existing session timing that conflicts with the given session timing.
    - 1b2. FitFlow prompts the user to enter a non-conflicting session timing.
    - 1b3. User re-enters the command.<br>
      Steps 1b1-1b3 repeat until a non-conflicting session timing is provided.<br>
      Use case resumes at step 2.

---

**Use case 3: Find Client**

**MSS**

1. User requests to find a specific client with a keyword.
2. FitFlow shows the list of clients with the given keyword on the app.<br>
   Use case ends.

**Extensions**

- 1a. The client being searched for does not exist.<br>
    - 1a1. FitFlow tells the User that it was unable to find a match.<br>
      Use case ends.
- 1b. The given client is invalid.<br>
    - 1b1. FitFlow shows an error message and prompts the user the format of the command.
    - 1b2. User re-enters the command.<br>
      Steps 1b1-1b2 repeat until the command is entered correctly.<br>
      Use case resumes at step 2.

---

**Use case 4: View Schedule**

**MSS**

1. User requests to view their schedule on a specific day.
2. FitFlow shows the list of clients that have a session for the specified day.<br>
   Use case ends.

**Extensions**

- 1a. The given day is invalid.
    - 1a1. FitFlow shows an error message and prompts the user the format of the command.
    - 1a2. User re-enters the command.<br>
      Steps 1a1-1a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 2.
- 1b. No clients were found to have sessions on given day.
    - 1b1. FitFlow tells the User that the schedule for the given day is empty.<br>
      Use case ends.

---

**Use case 5: Display Client's details**

**MSS**

1. User requests to <ins>find client (Use Case 3)</ins> or <ins>view schedule (Use Case 4)</ins>.
2. User requests to display the specific client's details.
3. FitFlow shows the details of the client on the app.<br>
   Use case ends.

**Extensions**

- 2a. The client requested to display does not exist.<br>
    - 2a1. FitFlow tells the User that it was unable to get the specified client's details.<br>
      Use case ends.
- 2b. The given client is invalid.<br>
    - 2b1. FitFlow shows an error message and prompts the user the format of the command.
    - 2b2. User re-enters the command.<br>
      Steps 2b1-2b2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.

---


**Use case 6: Delete Client**

**MSS**

1. User requests to <ins>find client (Use Case 3)</ins> or find the client to delete from the displayed client list.
2. User requests to delete the client.
3. FitFlow shows the client’s details to be deleted and prompts the user to confirm the decision to delete.
4. User confirms.
5. FitFlow shows the details of the client that has been deleted.<br>
   Use case ends.

**Extensions**

- 1a. The list is empty.<br>
  Use case ends.
- 2a. The given client is invalid.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.
- 4a. The user decides not to delete the client.
    - 4a1. FitFlow aborts the delete command.<br>
    Use case ends.

---

**Use case 7: Edit Client's Details**

**MSS**

1. User requests to <ins>find client (Use Case 3)</ins> or find the client to edit from the displayed client list.
2. User requests to edit the client's details.
3. FitFlow stores the new details of the client and indicates success.<br>
   Use case ends.

**Extensions**

- 1a. The list is empty.<br>
  Use case ends.
- 2a. The given client is invalid or the client details are given in the wrong format.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.
- 2b. User provides a session timing that conflicts with an existing session timing.
    - 2b1. FitFlow shows the existing session timing that conflicts with the given session timing.
    - 2b2. FitFlow prompts the user to enter a non-conflicting session timing.
    - 2b3. User re-enters the command.<br>
      Steps 2b1-2b3 repeat until a non-conflicting session timing is provided.<br>
      Use case resumes at step 3.

---

**Use case 8: Add session to client**

**MSS**

1. User requests to <ins>find client (Use Case 3)</ins> or <ins>view schedule (Use Case 4)</ins>.
2. User requests to <ins>edit client's details (Use Case 6)</ins> to add a session to the client.
3. FitFlow stores the new session details to the client details.<br>
   Use case ends.

**Extensions**

- 1a. The schedule is empty.<br>
  Use case ends.
- 2a. The given client is invalid or the session details are given in the wrong format.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.

---

**Use case 9: Delete session from client**

**MSS**

1. User requests to <ins>find client (Use Case 3)</ins> or <ins>view schedule (Use Case 4)</ins>.
2. User requests to <ins>edit client's details (Use Case 6)</ins> to delete a session from the client.
3. FitFlow removes the session details from the client details.<br>
   Use case ends.

**Extensions**

- 1a. The schedule is empty.<br>
  Use case ends.
- 2a. The given client is invalid or the session given is invalid.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.

---

**Use case 10: Modify session details for client**

**MSS**

1. User requests to <ins>find client (Use Case 3)</ins> or <ins>view schedule (Use Case 4)</ins>.
2. User requests to <ins>edit client's details (Use Case 6)</ins> to modify session details for the client.
3. FitFlow stores the new session details for the client.<br>
   Use case ends.

**Extensions**

- 1a. The schedule is empty.<br>
  Use case ends.
- 2a. The given client is invalid or the session details are given in the wrong format.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.

---

**Use case 11: Add session for multiple clients**

**MSS**

1. User requests to add a session and includes the clients' names involved with the session.
2. FitFlow stores the new session details for each of the specified client.<br>
   Use case ends.

**Extensions**

- 2a. The given client(s) is invalid or the session details are given in the wrong format.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.

---

**Use case 12: Delete session for multiple clients**

**MSS**

1. User requests to <ins>view schedule (Use Case 4)</ins> to find the session to delete.
2. User requests to delete the specific session.
3. FitFlow shows the session details and the clients involved in the session.
4. FitFlow will prompt the user to confirm the decision to delete.
5. User confirms.
6. FitFlow removes the session details for each of the specified clients.<br>
   Use case ends.

**Extensions**

- 2a. The given client(s) is invalid or the session details are given in the wrong format.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.
- 5a. The user decides not to delete the session.
    - 5a1. FitFlow aborts the delete session command.<br>
      Use case ends.

---

**Use case 13: Modify session for multiple clients**

**MSS**

1. User requests to <ins>view schedule (Use Case 4)</ins> to find the session to modify.
2. User requests to modify the specific session with new details.
3. FitFlow stores the new session details for the client.<br>
   Use case ends.

**Extensions**

- 2a. The given client(s) is invalid or the session details are given in the wrong format.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.

---

**Use case 14: Set reminder for session**

**MSS**

1. User requests to <ins>view schedule (Use Case 4)</ins> to find a session to set a reminder.
2. User requests to set a reminder for the specific session at a given timing before the session (e.g. 10 minutes).
3. FitFlow stores a reminder for the specified session.
4. FitFlow will prompt the user at the given timing before the session.

**Extensions**

- 2a. The given timing is invalid.
    - 2a1. FitFlow shows an error message and prompts the user the format of the command.
    - 2a2. User re-enters the command.<br>
      Steps 2a1-2a2 repeat until the command is entered correctly.<br>
      Use case resumes at step 3.

---

### Non-Functional Requirements

#### User Requirements:
1. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
2. The program should be able to work locally on someone’s device, without any connection to the Internet.
3. Someone who is a fast typist but isn’t familiar with command line interfaces should be able to pick up the application quickly.
4. The GUI should be designed for resolutions 1920x1080 or higher, with scales 100% and 125%, and work well on them.
5. The GUI should still be usable for resolutions 1280x720 and higher, and scales 150%.


#### Technical Requirements:
1. Should work on any mainstream OS as long as it has Java 17 or above installed.
2. The application should be contained within a single file, without the need for installation of extra dependencies.
3. The system should work on both 32-bit and 64-bit environments.

#### Data requirements:
1. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
2. Data should be stored locally, in a human-readable and human-editable file.

#### Performance requirements:
1. The application should be responsive to user input, and there shouldn’t be any input lag exceeding 1s.

#### Business/Domain rules:
1. Each contact must have at least a name and contact number.

#### Notes about project scope:
1. The application is not required to provide suggestions on schedules to the user.
2. The application is meant for a single-user.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Above Average Typing Seed**: Refers to a user capable of typing text (in natural language) at a faster rate than the typical user, enabling quick entry of commands or form data.
* **Client's Details**: This includes session details, training goals, medical history, gym location, and contact number.
* **Command Line Interface (CLI)**: A text-based interface that accepts typed commands. Users interact with the application by entering commands, rather than by clicking or tapping on-screen elements.
* **Contact**: An individual entry in the system’s address book or database, typically including (at minimum) a **name** and **contact number**.
* **Fast Typist (Fast Typing)**: A user who can input typed text swiftly, increasing overall efficiency when using a CLI-based or text-based system.
* **Graphical User Interface (GUI)**: A visual, interactive interface that uses elements such as windows, buttons, and menus. Users interact by pointing, clicking, or tapping, rather than typing commands.
* **Human-Editable Format**: A data storage format (e.g., CSV, JSON, YAML) that can be opened in any text editor and modified directly by a human without needing specialized software or database tools.
* **Human-Readable Format**: A data storage format that is **easily understood** by users (e.g., structured text instead of proprietary binary formats). This makes it simpler for users to inspect or debug stored data.
* **Input Lag**: The delay between the user performing an action (e.g., typing a command, clicking a button) and the system responding. A well-optimized application keeps this delay under 1 second to feel “instant” to the user.
* **Local Data Storage**: Storing all user or application data on the same device that the application is running on. This setup does not require an internet connection or external servers.
* **Performance Sluggishness**: A noticeable delay in the application’s responsiveness, typically when dealing with large datasets or intense processing tasks. The requirement states that managing up to 1000 contacts should not cause any discernible slowdown.
* **Scale Factor**: The magnification or zoom level applied to on-screen elements (e.g., 100%, 125%, 150%). This is relevant for accessibility and ensuring proper display on high-resolution monitors.
* **Screen Resolution**: The pixel dimensions of the display (e.g., 1920×1080, 1280×720). Higher resolutions typically allow more UI elements to appear clearly on screen. The application must remain usable and visually clear at both high and moderate resolutions.
* **Single-File Application**: An application distributed as a single executable or JAR file, avoiding the need for extra installations or additional dependencies on the user’s system.
* **Single-User Application**: Intended for use by one person at a time, with no requirement for multi-user logins or collaborative functionality.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file<br>
   Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
   Expected: The most recent window size and location is retained.

3. Exit program

   1. Entering exit command. Close the window.

   2. Double-click the jar file<br>
   Expected: Shows the GUI with contacts. The most recent window size and location is retained.

### Deleting a client

1. Deleting a client while all clients are being shown

   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact is shown in the status message.

   3. Test case: `delete x` (where x is larger than the list size)<br>
      Expected: No person is deleted. Error details of invalid client index shown in the status message.

   4. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details of incorrect format and Right usages are shown in the status message.

   5. Other incorrect delete commands to try: `delete`, `delete y`, `...` (where y is not a positive integer)<br>
      Expected: Similar to previous.

### Displaying a client

1. Displaying a client while all clients are being shown

    1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.

    2. Test case: `display 1`<br>
       Expected: First contact is displayed from the list. Details of the client is shown in the status message.

    3. Test case: `display x` (where x is larger than the list size)<br>
       Expected: No person is displayed. Error details of invalid client index shown in the status message.

    4. Test case: `display 0`<br>
       Expected: No person is displayed. Error details of incorrect format and Right usages are shown in the status message.

    5. Other incorrect display commands to try: `display`, `display y`, `...` (where y is not a positive integer)<br>
       Expected: Similar to previous.

### Finding a client

1. Finding a client in the client list

    1. Test case: `find x` (where x is a complete part of client's name)<br>
    Expected: Client list only show the contacts that contains the name. Number of clients listed message is shown in the status message.

    2. Test case: `find x y` (where x and y are complete part of different client's name)<br>
    Expected: Client list only show the contacts that contains the name. Number of clients listed message is shown in the status message.

    3. Test case: `find z` (where z is not part of any client's name)<br>
    Expected: Client list is empty. No clients listed message is shown in the status message.

### Saving data

1. Dealing with missing data files

   1. Prerequisites: Executed the [final FitFlow release] jar file.

   2. Open the FitFlow home folder. Delete the Data folder.

   3. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size is reset.

2. Dealing with corrupted data files

   1. Prerequisites: Executed the [final FitFlow release] jar file.

   2. Open the FitFlow home folder. Open the Data folder. Open the addressbook.json.

   3. In the open addressbook.json. Remove the curly bracket ({) at line 1. Close and Save the file.

   4. Double-click the jar file<br>
   Expected: Shows the GUI without a set of sample contacts. The most recent window size and location is retained.
