//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;


import io.github.novacrypto.DatatypeConverter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class PayloadRegisterIdentification {
    private String Id;
    private String Sign;
    private PayloadRegisterIdentification.RegisterIdentificationContent[] Contents;
    private String IdPrivKey;
    private String programHash;

    public PayloadRegisterIdentification() {
    }

    public String getProgramHash() {
        return this.programHash;
    }

    public void setProgramHash(String programHash) {
        this.programHash = programHash;
    }

    public String getId() {
        return this.Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getIdPrivKey() {
        return this.IdPrivKey;
    }

    public void setIdPrivKey(String idPrivKey) {
        this.IdPrivKey = idPrivKey;
    }

    public String getSign() {
        return this.Sign;
    }

    public void setSign(String sign) {
        this.Sign = sign;
    }

    public PayloadRegisterIdentification.RegisterIdentificationContent[] getContents() {
        return this.Contents;
    }

    public void setContents(PayloadRegisterIdentification.RegisterIdentificationContent[] contents) {
        this.Contents = contents;
    }

    public String toString() {
        return "PayloadRegisterIdentification{Id='" + this.Id + '\'' + ", Sign='" + this.Sign + '\'' + ", Contents=" + Arrays.toString(this.Contents) + '}';
    }

    public void Serialize(DataOutputStream o) throws IOException {
        Util.WriteVarUint(o, (long)this.Id.length());
        o.write(this.Id.getBytes());
        byte[] signByte = this.Sign.getBytes();
        Util.WriteVarBytes(o, signByte);
        Util.WriteVarUint(o, (long)this.Contents.length);

        for(int i = 0; i < this.Contents.length; ++i) {
            this.Contents[i].Serialize(o);
        }

    }

    public class RegisterIdentificationValue {
        private String DataHash;
        private String Proof;

        public RegisterIdentificationValue() {
        }

        public String getDataHash() {
            return this.DataHash;
        }

        public void setDataHash(String dataHash) {
            this.DataHash = dataHash;
        }

        public String getProof() {
            return this.Proof;
        }

        public void setProof(String proof) {
            this.Proof = proof;
        }

        public String toString() {
            return "RegisterIdentificationValue{DataHash='" + this.DataHash + '\'' + ", Proof='" + this.Proof + '\'' + '}';
        }

        public void Serialize(DataOutputStream o) throws IOException {
            byte[] DataHashByte = DatatypeConverter.parseHexBinary(this.DataHash);
            if (DataHashByte.length != 32) {
                throw new RuntimeException("DataHash must be 32 bytes");
            } else {
                Uint256 uint256 = new Uint256(DataHashByte);
                uint256.Serialize(o);
                Util.WriteVarUint(o, (long)this.Proof.length());
                o.write(this.Proof.getBytes());
            }
        }
    }

    public class RegisterIdentificationContent {
        private String PATH;
        private PayloadRegisterIdentification.RegisterIdentificationValue[] Values;

        public RegisterIdentificationContent() {
        }

        public String getPATH() {
            return this.PATH;
        }

        public void setPATH(String PATH) {
            this.PATH = PATH;
        }

        public PayloadRegisterIdentification.RegisterIdentificationValue[] getValues() {
            return this.Values;
        }

        public void setValues(PayloadRegisterIdentification.RegisterIdentificationValue[] values) {
            this.Values = values;
        }

        public String toString() {
            return "RegisterIdentificationContent{PATH='" + this.PATH + '\'' + ", Values=" + Arrays.toString(this.Values) + '}';
        }

        public void Serialize(DataOutputStream o) throws IOException {
            Util.WriteVarUint(o, (long)this.PATH.length());
            o.write(this.PATH.getBytes());
            Util.WriteVarUint(o, (long)this.Values.length);

            for(int i = 0; i < this.Values.length; ++i) {
                this.Values[i].Serialize(o);
            }

        }
    }
}
