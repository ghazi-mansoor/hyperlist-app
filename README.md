# HyperList ReadMe
HyperList is a time and stress management native Android application tailored for Adult ADHD users. The application was created as part of the CSCI 5708: Mobile Computing course (Winter 2020) at Dalhousie University.

The app’s codebase follows the Model-View-ViewModel architectural pattern alongside JetPack navigation, Room persistence, and WorkManager components implemented in Kotlin. 

**IMAGE**

## UX Development
HyperList’s UX maximises focus, motivation, and positivity through a combination of time-boxing, the Pomodoro technique, positive reinforcement, and intervention.

The app’s UX was developed after extensive usability research that included interviews of adult ADHD patients, and hypothesis testing with both low and fidelity prototypes created with Figma.

## Core Features
### Daily To-do List with Natural Language Input
A daily to-do list with natural language input to capture time-boxed daily tasks. Users can create tasks, assign priorities, and time-blocks through regex-based natural language input.

The natural language input avoids breaking the user’s focus and attention by not forcing them to open a new dialog box or screen.

**IMAGE**

### Pomodoro Timer
Uses the psychologically proven Pomodoro technique for increasing focus, eliminating distractions, and enhancing the motivation to perform a task. 

**IMAGE**

### Deep Linked Reminders
Daily reminders that are deep-linked to the Pomodoro timer screen (fragment).  Tapping the notification automatically starts the pomodoro timer. This reduces the friction and potential the lack of motivation that can prevent the user from starting the timer by first opening the app and navigating to the timer screen.

**IMAGE**

### Bar Code Scanner Alarm
ADHD users have a hard time waking up due to a lack of dopamine early in the morning. The interviewed users told stories of snoozing their alarms hours at a stretch.

HyperList features a bar-code based daily alarm that cannot be turned off unless the user wakes up, moves some distance, and scans a bar-code.

**IMAGE**

### Tips and Help
The app provides instant access to proven tips and techniques for quickly managing  ADHD associated issues such as: 
	1. Anxiety
	2. Feeling overwhelmed
	3. Lack of focus
	4. Motivation

**IMAGE**
