package com.ratemytree.rmt.restapi;

/**
 * David Schilling - davejs92@gmail.com
 */
public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException(String message) {
        super(message);
    }
}