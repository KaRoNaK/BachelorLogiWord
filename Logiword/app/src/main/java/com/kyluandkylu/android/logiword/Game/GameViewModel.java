package com.kyluandkylu.android.logiword.Game;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeSet;

public class GameViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> currentVal;
    private MutableLiveData<String> currentValText;
    private MutableLiveData<ArrayList<Character>> currentLetters;
    private MutableLiveData<String> currentWord;

    private ArrayList<Move> moves;
    private TreeSet<String> words;
    private String yourWordText;
    private Timestamp startTime;
    private boolean justRestarted;

    public GameViewModel(@NonNull Application application) {
        super(application);
        currentVal = new MutableLiveData<>();
        currentVal.setValue(0);
        currentValText = new MutableLiveData<>();
        currentValText.setValue("0");
        currentLetters = new MutableLiveData<>();
        currentLetters.setValue(new ArrayList<Character>());
        currentWord = new MutableLiveData<>();
        currentWord.setValue(yourWordText);
        words = WordList.getInstance().getWordsTree();
        moves = new ArrayList<>();
        justRestarted = true;
        startTime = new Timestamp(new java.util.Date().getTime());
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void removeLeftDigit() {
        int current = currentVal.getValue();
        if (current >= 10) {
            String temp = Integer.toString(current);
            current = Integer.parseInt(temp.substring(0, temp.length() - 1));
            moves.add(new Calculation("<<", currentVal.getValue(), current));
            currentVal.setValue(current);
        }
    }

    public void removeRightDigit() {
        int current = currentVal.getValue();
        if (current >= 10) {
            String temp = Integer.toString(current);
            current = Integer.parseInt(temp.substring(1));
            moves.add(new Calculation(">>", currentVal.getValue(), current));
            currentVal.setValue(current);
        }
    }

    public boolean isValueLongerThenOneDigit() {
        String v = Integer.toString(currentVal.getValue());
        return v.length() > 1;
    }

    public MutableLiveData<String> getCurrentWord() {
        return currentWord;
    }

    public boolean isCurrentWordValid() {
        if (yourWordText.equals("Your word")) {
            return words.contains(currentWord.getValue().toLowerCase()) && currentWord.getValue().length() >= 3;
        } else {
            boolean ret = currentWord.getValue().toLowerCase().equals(yourWordText.toLowerCase()) && !justRestarted && moves.size() > 0;
            justRestarted = false;
            return ret;
        }

    }

    private void addLetterToWord(char letter) {
        if (currentWord.getValue().equals(yourWordText)) {
            currentWord.setValue("");
        }
        currentWord.setValue(currentWord.getValue() + letter);
    }

    public void restartWord() {
        if (!currentWord.getValue().toLowerCase().equals(yourWordText.toLowerCase())) {
            String old = currentWord.getValue();
            for (char c : old.toCharArray()) {
                currentLetters.getValue().add(c);
            }
            justRestarted = true;
            currentLetters.setValue(currentLetters.getValue());
            currentWord.setValue(yourWordText);
        }
    }

    public MutableLiveData<ArrayList<Character>> getCurrentLetters() {
        return currentLetters;
    }

    public void addLetter(char letter) {
        currentLetters.getValue().add(letter);
        moves.add(new LetterSetChange(letter));
        currentLetters.setValue(currentLetters.getValue());
    }

    public void selectLetter(int index) {
        char selected = currentLetters.getValue().remove(index);
        addLetterToWord(selected);
        currentLetters.setValue(currentLetters.getValue());
    }

    public MutableLiveData<String> getCurrentValText() {
        return currentValText;
    }

    public void changeCurrentValText(String newText) {
        currentValText.setValue(newText);
    }

    public LiveData<Integer> getCurrentVal() {
        return currentVal;
    }

    public void changeVal(int newVal) {
        currentVal.setValue(newVal);
    }

    public void restartVal() {
        moves.add(new Calculation("CE", currentVal.getValue(), 0));
        currentVal.setValue(0);
    }

    public void negateVal() {
        moves.add(new Calculation("+/-", currentVal.getValue(), currentVal.getValue() * -1));
        currentVal.setValue(currentVal.getValue() * -1);
    }

    public void performOperation(String type, int val) {
        int v = currentVal.getValue();
        switch (type) {
            case "+":
                currentVal.setValue(v + val);
                break;
            case "-":
                currentVal.setValue(v - val);
                break;
            case "*":
                currentVal.setValue(v * val);
                break;
            case "/":
                if (val != 0) {
                    currentVal.setValue(v / val);
                }
                break;
            case "^":
                currentVal.setValue((int) (Math.pow(v, val)));
                break;
        }
        moves.add(new Calculation(type, v, val));
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setYourWordText(String yourWordText) {
        this.yourWordText = yourWordText;
        currentWord.setValue(yourWordText);
    }
}
