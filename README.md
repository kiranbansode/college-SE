# Scientific Calculator by Kiran Bansode

A desktop scientific calculator written in Java. The window uses Swing, which is included with the JDK, so the project has no extra libraries. The math lives in its own classes. Buttons only send commands to the engine.

## Requirements

- JDK 8 or newer (`javac` and `java` available on the PATH)
- No Maven, Gradle, or third-party jars

## Compile and run

From this project folder:

```bat
javac -d out -sourcepath src src\CalculatorApp.java
java -cp out CalculatorApp
```

Compiled class files are written to the `out` folder. `CalculatorApp` is the main class.

## What it can do

- Add, subtract, multiply, divide, power, and parentheses
- `sin`, `cos`, `tan`, `asin`, `acos`, `atan`, `log` (base 10), `ln`, square root, square, reciprocal, absolute value, factorial, and percent
- Constants `π` and `e`
- Degrees / radians toggle (`DEG` / `RAD`), default degrees
- Memory: `MC`, `MR`, `M+`, `M−`
- `Ans` recalls the last result committed with `=`
- Keyboard: digits, `+ − * / ^ % ! ( )`, Enter for equals, Backspace, and Escape for all clear

Percent divides that value by 100, so `50%` is `0.5`. `Ans` stays `0` until you press `=` once. If the expression already ends with a number, `Ans` is inserted with a multiply sign.

## Layout

```
src/
  CalculatorApp.java          starts the window
  ui/                         Swing frame, display, and keypad
  model/                      expression, angle mode, and memory
  eval/                       lexer, parser, and number formatting
  op/                         Operation interface and arithmetic
  op/functions/               one class per scientific function
  error/                      errors shown on the display
```

## Object-oriented design

| Idea | Where |
|---|---|
| Encapsulation | `CalculatorEngine` and `MemoryRegister` keep state private |
| Abstraction | `Operation` is the interface every function implements |
| Inheritance | `UnaryOperation` and `BinaryOperation` share argument checks |
| Polymorphism | `Sin`, `Add`, `Factorial`, and the other classes each implement `apply` |
| Composition | `CalculatorFrame` is built from a display, a keypad, and an engine |
