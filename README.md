# Pre-work - *ToDoList App*

**ToDoList** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Uduak Obong-Eren**

Time spent: **6** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Status messages using Toast!
* [x] Checkbox that allows user mark an item as completed
* [x] Toggle feature that allows user view a list of all to-do items versus incomplete to-do list items

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/iD6ci2t.gif' width='600' title='ToDoList Video Walkthrough' width='' alt='ToDoList Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** [Exciting platform with lots of existing functionality that one can leverage. Similar to building web apps using templates (Handlebars, Velocity) where the templates handle page layout and the Activity files handle the interaction].

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** [The ArrayAdapter does the work of converting each ToDo item in the items collection into a view. It's `getView` method returns a View that displays the data at the specified position in the collection].

## Notes

* The first challenge I had was while working on persisting the ToDo items in the SQLite database. I needed to view the structure of the tables I had created and I initially had trouble connecting to my app's via the adb shell, particularly because I had errors starting up the Android Device Monitor.

* I also had some challenge with the correct type to use for date field in the app's database formatting the date was also a bit of a challenge. 

## License

    Copyright [2017] [Uduak Obong-Eren]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.