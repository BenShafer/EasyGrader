# EasyGrader

EasyGrader is an Android application I made for a school project. It is to be used as an electronic gradebook for instructors. Instructors can use this application to manage the assignments for their courses as well as the grades for the assignments. Once a course has concluded, the instructor is then able to finalize the grades for that course. The application is also used by administrators to manage courses and student enrollment. Be sure to check out the [wiki](https://github.com/BenShafer/EasyGrader/wiki) for more information.

Demo video going over the [use cases](https://github.com/BenShafer/EasyGrader/wiki/Use-Cases): https://youtu.be/jnCoItcNxqo


## Design

The app is designed using MVVM (Model View ViewModel) architecture. Persistent storage is done using SQLite with the Room library. Activities are started using an Intent Factory class. Activities make use of Fragments to draw the user interface. LiveData is used often for data items and lists utilize RecyclerView adapters to display the data.

## Screenshots
![Screenshots of the app](https://github.com/BenShafer/EasyGrader/blob/main/diagrams/Screenshots.png)
