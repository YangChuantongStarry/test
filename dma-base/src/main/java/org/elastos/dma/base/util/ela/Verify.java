//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import com.alibaba.fastjson.JSONObject;

import io.github.novacrypto.DatatypeConverter;

import org.elastos.dma.base.util.common.ErrorCode;

public class Verify {
    public Verify() {
    }

    public static void verifyParameter(Verify.Type type, JSONObject jsonObject) throws SDKException {
        switch(type) {
        case PrivateKeyUpper:
            Object PrivateKeyUpper = jsonObject.get(type.getValue());
            if (PrivateKeyUpper == null) {
                throw new SDKException(ErrorCode.PrivateKeyNotNull);
            }

            if (((String)PrivateKeyUpper).length() != 64) {
                throw new SDKException(ErrorCode.InvalidPrpvateKey);
            }

            try {
                DatatypeConverter.parseHexBinary((String)PrivateKeyUpper);
                break;
            } catch (Exception var21) {
                throw new SDKException(ErrorCode.InvalidPrpvateKey);
            }
        case BlockHashUpper:
            Object BlockHashUpper = jsonObject.get(type.getValue());
            if (BlockHashUpper == null) {
                throw new SDKException(ErrorCode.BlockHashNotNull);
            }

            if (((String)BlockHashUpper).length() != 64) {
                throw new SDKException(ErrorCode.InvalidBlockHash);
            }

            try {
                DatatypeConverter.parseHexBinary((String)BlockHashUpper);
                break;
            } catch (Exception var20) {
                throw new SDKException(ErrorCode.InvalidBlockHash);
            }
        case PrivateKeyLower:
            Object PrivateKeyLower = jsonObject.get(type.getValue());
            if (PrivateKeyLower == null) {
                throw new SDKException(ErrorCode.PrivateKeyNotNull);
            }

            if (((String)PrivateKeyLower).length() != 64) {
                throw new SDKException(ErrorCode.InvalidPrpvateKey);
            }

            try {
                DatatypeConverter.parseHexBinary((String)PrivateKeyLower);
                break;
            } catch (Exception var19) {
                throw new SDKException(ErrorCode.InvalidPrpvateKey);
            }
        case TxidLower:
            Object TxidLower = jsonObject.get(type.getValue());
            if (TxidLower == null) {
                throw new SDKException(ErrorCode.TxidnotNull);
            }

            if (((String)TxidLower).length() != 64) {
                throw new SDKException(ErrorCode.InvalidTxid);
            }
            break;
        case PasswordLower:
            Object PasswordLower = jsonObject.get(type.getValue());
            if (PasswordLower == null) {
                throw new SDKException(ErrorCode.PasswordNotNull);
            }

            if (!(PasswordLower instanceof String)) {
                throw new SDKException(ErrorCode.InvalidPassword);
            }
            break;
        case AddressLower:
            Object AddressLower = jsonObject.get(type.getValue());
            if (AddressLower == null) {
                throw new SDKException(ErrorCode.AddressNotNull);
            }

            try {
                byte[] sh = Util.ToScriptHash((String)AddressLower);
                if (sh[0] != 33 && sh[0] != 18 && sh[0] != 103) {
                    throw new SDKException(ErrorCode.InvalidAddress);
                }
                break;
            } catch (Exception var18) {
                throw new SDKException(ErrorCode.InvalidAddress);
            }
        case ChangeAddress:
            Object ChangeAddress = jsonObject.get(type.getValue());
            if (ChangeAddress == null) {
                throw new SDKException(ErrorCode.ChangeAddressNotNull);
            }

            try {
                byte[] sh = Util.ToScriptHash((String)ChangeAddress);
                if (sh[0] != 33 && sh[0] != 18) {
                    throw new SDKException(ErrorCode.InvalidChangeAddress);
                }
                break;
            } catch (Exception var17) {
                throw new SDKException(ErrorCode.InvalidChangeAddress);
            }
        case Host:
            Object Host = jsonObject.get(type.getValue());
            if (Host == null) {
                throw new SDKException(ErrorCode.HostNotNull);
            }
            break;
        case AmountLower:
            Object AmountLower = jsonObject.get(type.getValue());
            if (AmountLower == null) {
                throw new SDKException(ErrorCode.AmountNotNull);
            }

            if (!(AmountLower instanceof Long) && (!(AmountLower instanceof Integer) || (Integer)AmountLower < 0)) {
                throw new SDKException(ErrorCode.InvalidAmount);
            }
            break;
        case MUpper:
            Object MUpper = jsonObject.get(type.getValue());
            if (MUpper == null) {
                throw new SDKException(ErrorCode.MNotNull);
            }

            if (MUpper instanceof Long || MUpper instanceof Integer && (Integer)MUpper >= 0) {
                break;
            }

            throw new SDKException(ErrorCode.InvalidM);
        case Fee:
            Object fee = jsonObject.get(type.getValue());
            if (fee == null) {
                throw new SDKException(ErrorCode.FeeNotNull);
            }

            if (!(fee instanceof Long) && (!(fee instanceof Integer) || (Integer)fee < 0)) {
                throw new SDKException(ErrorCode.InvalFee);
            }
            break;
        case Confirmation:
            Object Confirmation = jsonObject.get(type.getValue());
            if (Confirmation == null) {
                throw new SDKException(ErrorCode.ConfirmationNotNull);
            }

            if (!(Confirmation instanceof Long) && (!(Confirmation instanceof Integer) || (Integer)Confirmation < 0)) {
                throw new SDKException(ErrorCode.InvalConfirmation);
            }
            break;
        case IndexLower:
            Object IndexLower = jsonObject.get(type.getValue());
            if (IndexLower == null) {
                throw new SDKException(ErrorCode.IndexNotNull);
            }

            if (!(IndexLower instanceof Long) && (!(IndexLower instanceof Integer) || (Integer)IndexLower < 0)) {
                throw new SDKException(ErrorCode.InvalidIndex);
            }
            break;
        case RecordTypeLower:
            Object RecordTypeLower = jsonObject.get(type.getValue());
            if (RecordTypeLower == null) {
                throw new SDKException(ErrorCode.RecordTypeNotNull);
            }

            if (Util.isChinese((String)RecordTypeLower)) {
                throw new SDKException(ErrorCode.InvalidRecordType);
            }
            break;
        case RecordDataLower:
            Object RecordDataLower = jsonObject.get(type.getValue());
            if (RecordDataLower == null) {
                throw new SDKException(ErrorCode.RecordDataNotNull);
            }
        }

    }

    public static enum Type {
        PrivateKeyLower("privateKey"),
        PrivateKeyUpper("PrivateKey"),
        AddressLower("address"),
        ChangeAddress("ChangeAddress"),
        AmountLower("amount"),
        MUpper("M"),
        Host("Host"),
        Fee("Fee"),
        Confirmation("Confirmation"),
        TxidLower("txid"),
        IndexLower("index"),
        PasswordLower("password"),
        RecordTypeLower("recordType"),
        RecordDataLower("recordData"),
        BlockHashUpper("BlockHash");

        private String type;

        private Type(String t) {
            this.type = t;
        }

        public String getValue() {
            return this.type;
        }
    }
}
