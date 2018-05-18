# The Mansion

# About Game
- Main Objective
The main objective to finish the game is to find the key and player will get notification
that the character succeed escape the mansion. To know where is the key player need to focus
on finding hint and take a notes every journey each level and room in the mansion.

- Gameplay
1.  Player need to first their name as the game will record the times finish when player finished the game.
2.  Player decide the difficulty.
3.	The game will give player instruction to the player about how to play the game and how to utilize feature inside the game.
4.	As the player explores the rooms, player can take a note then find the key from hint gathered.
5.	There will be various rooms can be visit each level with a questions or action to do to answer.
6.  The character have a life point base on story said 'candle' as their life in the game.
7.	Each room have their own related question. There will be many traps to make player life point decrease. And some limited question to gain life point.
8.	If the player succeed to find the key, player will get notification that player already finish the game and it will recorded base on seconds take times to finish.

- Stages and Interactivity
The game area of play will be determined after the player visited each room. The rooms will remain question mark if not get visited by the player character.
room to room and each level of the mansion structure.

There will be 3 levels in the mansion:
1. Lower Ground level
2. Ground level
3. Upper Ground level

- For each level, there will be at least 3 rooms available at the early stages
of the development.

- The player can move around the room with answering a question or decide a decision
about the room. Fail to answer the room question or make mistake in decision will
player character get hurt and lose life points..

- The player will be given a notes to write anything that player think is an evidence to find the key.

# Platform
minimum Android 5.0(Lollipop)

# Repository
https://github.com/bobjaeger/TheMansion

# Team
Bobby Putra	SID215339949
Dennis Darwis SID216280619

# Game data directory
- JSON data will be under this path /game_feature/src/main/assets
- Picture/Image will be under this path /game_feature/src/main/res/drawable
- App icon will be under this path /base/src/main/res/(inside each size format folder)
- Audio file will be under this path /game_feature/src/main/res/raw

# API References
### Data model classes
RoomModel
```
public RoomModel(int id, String roomName) {
        this.id = id;
        this.roomName = roomName;
}
```
QuestionModel
```
public QuestionModel(int qid, String question, List<AnswerModel> answerLists) {
        this.qid = qid;
        this.question = question;
        this.answerLists = answerLists;
}
```
AnswerModel
```
public AnswerModel(int nextQid, String answer, String anotherDialog, int effect) {
        this.nextQid = nextQid;
        this.answer = answer;
        this.anotherDialog = anotherDialog;
        this.effect = effect;
}
```
HighScoreModel
```
public RoomModel(int id, String roomName) {
        this.id = id;
        this.roomName = roomName;
}
```
### Important methods and classes
- loadJSONFromAsset(): to read external JSON game file.
- parseRoom(): to parse the JSON room file while on LobbyInterface.
- loadQuestionById() and loadFirstQuestion(): to parse the JSON question file while on RoomInterface.

# Instruction to run the app in Android emulator
- To run the app in emulator you must need to have Android Studio installed
- And make sure you have VT-X or Intel Virtualization enabled in BIOS of your PC/Laptop
- If all above already done, the next thing is you need to create the emulator in Android Studio
- Our app using the latest Android Oreo to run, so you will set the API for the emulator above API25
- After done with the emulator now you just need to emulate the app to run and test the app
- If you're unsure how to do it you can see the full guide how to run the app in emulator on this link
link -https://developer.android.com/studio/run/emulator

# License
- All the license for visual and audio we use can be found in the project main directory there was notepad called license.txt

# Latest Henry comments 27/April (replied)
- missing compile instructions and directory explanation in readme (eg where do I find your data?)
reply: Yes we understand about that, We have many commits in this week but we still need to be done data works, we'll
try to give all information as we can in this readme doc.

# Henry comments 13/April (replied)
- You've started, which is good. reply: Thanks
- Not enough commits + change log items to get above a Pass at this frequency. reply: will update all the data this week
- I can't find your game's data files (e.g. JSON files)? Create a root folder "data/", and put all your text-based data files in there.
reply: for JSON and others will be under /game_feature/src/main/assets
need feedback: Do I need to move it to another short path?
