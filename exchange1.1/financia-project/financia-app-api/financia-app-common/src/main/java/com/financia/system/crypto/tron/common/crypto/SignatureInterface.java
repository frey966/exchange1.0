package com.financia.system.crypto.tron.common.crypto;

public interface SignatureInterface {
    boolean validateComponents();

    byte[] toByteArray();
}