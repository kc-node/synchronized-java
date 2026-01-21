# synchronized-java

## Overview

This project demostrate solutions to classic concurrency problems using **Java MultiThreading**. 
Java Multi-Threading and Concurrency Project: Using Reusable Barrier, Blocking Queues and Producer-Consumer implementations demonstrating synchronization, thread safety and concurrency control.

It includes three tasks:
1. **Resuable Barriers** - A barrier for threads to wait until all threads reach it then continue together.
2. **Blocking Queues** - Implementation of coarse-grained and fined-grained blocking queues for producer-consumer.
3. **Producer-Consumer with Poison-Pill** - A simple producer-consumer problem using BlockingQueue and poison pills to stop consumers.

# Project Structure
- Task1.java - ReusableBarrier implementation
- Task2.java - CoarseGrainedBlockingQueue and FineGrainedBlockingQueue implementations
- Task3.java - Producer and Consumer implementation using poison pills
- Main.java - Test

# Tech Stack
**Languages:** Java
**Libraries:** 'java.util.concurrent', 'java.util.concurrent.locks'

## How to Run
1. Compile your Java  files:
     javac *.java
     java Main
