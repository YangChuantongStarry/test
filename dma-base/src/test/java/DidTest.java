import com.alibaba.fastjson.JSONObject;
import org.elastos.dma.base.entity.did.DIDAccount;
import org.elastos.dma.base.entity.ela.ElaAccount;
import org.elastos.dma.base.entity.eth.EthAccount;
import org.elastos.dma.base.passport.DID;
import org.elastos.dma.base.util.HdSupport;
import org.elastos.dma.base.util.Utility;
import org.elastos.dma.base.wallet.ElaWallet;
import org.elastos.dma.base.wallet.EthWallet;
import org.elastos.dma.utility.entity.ela.ReturnMsgEntity;
import org.elastos.dma.utility.entity.ela.SignDataEntity;
import org.elastos.dma.utility.exception.ApiException;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Collections;

public class DidTest {


//kkk
    @Test
    public void test() {
        byte[] bytes = "1234".getBytes();
        String s = DatatypeConverter.printHexBinary(bytes);
        System.out.println("s = " + s);


        String s1 = printHexBinary(bytes);
        System.out.println("s1 = " + s1);


    }

    String nodeUrl = "http://192.168.1.112:8545";

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }


    @Test
    public void testEthBalance() throws IOException {
        EthWallet ethWalletService = new EthWallet("https://ropsten.etherscan.io");
        String balance = ethWalletService.balance("0xf63af9cb34651a0061f3efd7e2c5e7ceb5aad89a");
        System.out.println("balance = " + balance);
    }

    @Test
    public void contractInfo() {
        ElaAccount elaAccount = new ElaAccount("5368C7007929A23E26720D670738E7B64849E11CD872B7B1B8EA9DDF50B74F6A");
        String address = elaAccount.getAddress();
        System.out.println("address = " + address);


    }



    @Test
    public void testBalance() throws IOException {
        EthWallet ethWalletService = new EthWallet(nodeUrl);
        String balance = ethWalletService.balance("0xcef63da9905b6b2c74146a6cdbd7a7a533ebddee");
        System.out.println("balance = " + balance);
    }

    @Test
    public void testCreateDid() throws ApiException {
        DID did = new DID("");
        DIDAccount didAccount = did.create(HdSupport.MnemonicType.CHINESE);


        String mnemonic = didAccount.getMnemonic();

        ElaWallet elaWallet = new ElaWallet("");

        ElaAccount elaAccount = elaWallet.exportByMnemonic(mnemonic);
        System.out.println("elaAccount = " + elaAccount);

        EthWallet ethWallet = new EthWallet("");

        EthAccount ethAccount = ethWallet.exportByMnemonic(mnemonic);
        System.out.println("ethAccount = " + ethAccount);
    }

    @Test
    public void testTransfer() throws Exception {
        String privateKey = "1F1A4EA2BB3DBB1FA898B335B22EF2CFB5E3C9FA5D5672A61A83CA47264014C1";
        String toPrivateKey = "5368C7007929A23E26720D670738E7B64849E11CD872B7B1B8EA9DDF50B74F6A";


        ElaAccount elaAccount = new ElaAccount(privateKey);
        ElaWallet elaWalletService = new ElaWallet("http://54.64.220.165:21334");
        String balance = elaWalletService.balance(elaAccount.getAddress());

        System.out.println("balance = " + balance);
        ElaAccount to = new ElaAccount(toPrivateKey);
        String toAddress = to.getAddress();
        System.out.println("toAddress = " + toAddress);

//        String transferHash = elaWalletService.transfer(privateKey, toAddress, "5" );
//        System.out.println("transferHash = " + transferHash);

        while (true) {
            String balance1 = elaWalletService.balance(toAddress);
            System.out.println("balance1 = " + balance1);
            Thread.sleep(1000L);
        }
//        String transferHash = elaWalletService.transfer(privateKey, toAddress, 100000000 * 0.1 + "");
//        String transferHash = elaWalletService.transfer(privateKey, toAddress, 1 + "");
//        System.out.println("transferHash = " + transferHash);

    }


    @Test
    public void testSetDidInfo() throws Exception {

        String privateKey = "1F1A4EA2BB3DBB1FA898B335B22EF2CFB5E3C9FA5D5672A61A83CA47264014C1";


        ElaAccount elaAccount = new ElaAccount(privateKey);
        String nodeUrl = "http://54.64.220.165:21334";
        ElaWallet elaWalletService = new ElaWallet(nodeUrl);
        String balance = elaWalletService.balance(elaAccount.getAddress());

        DID passportService = new DID(nodeUrl);
        JSONObject map = new JSONObject();
        String key = "name";
        map.put(key, "bob");
        map.put("password", "123456");
        String s = "56b53567b1fb71e49fa0ab31d57283ba33e731661ef9ed2bc892e6a0d1b72021";// passportService.setDIDInfo(privateKey, map);

        while (true) {
            try {
                System.out.println("balance = " + balance);
                JSONObject didInfo = passportService.getDIDInfo(Collections.singletonList(s), key);
                System.out.println("didInfo = " + didInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test // 86.57336100
    public void testSign() throws Exception {
        String privateKey = "1F1A4EA2BB3DBB1FA898B335B22EF2CFB5E3C9FA5D5672A61A83CA47264014C1";


        ElaAccount elaAccount = new ElaAccount(privateKey);
        String nodeUrl = "http://54.64.220.165:21334";
        ElaWallet elaWalletService = new ElaWallet(nodeUrl);
        String balance = elaWalletService.balance(elaAccount.getAddress());

        System.out.println("balance = " + balance);


        DID passportService = new DID(nodeUrl);

        SignDataEntity entity = new SignDataEntity();
        entity.setMsg("test");
        entity.setPrivateKey(privateKey);
        SignDataEntity signDataEntity = JSONObject.parseObject("{\"msg\":\"74657374\",\"pub\":\"02FAAFC78A6C883BD2E5799E14E341A734A7069E4ACEC1495F9E22F79EC6C3505E\",\"sig\":\"362AA52F02BC26B21D889241B886301FD57DD3CD8E9176EFE4E6389BD356A9EC9B9CFB16724DD83446D228829A5C7FA4EE99B52FE4E0AF5897083FA8E8BFDBCC\"}" ,SignDataEntity.class) ;//passportService.signInfo(entity);

        String toJSONString = JSONObject.toJSONString(signDataEntity);
        System.out.println("jsonString：" + toJSONString);


        ReturnMsgEntity<Boolean> verify = passportService.verify(signDataEntity);

        String s = JSONObject.toJSONString(verify);

        System.out.println("s = " + s);

    }
    @Test
    public void testCreateDid1() throws ApiException {
        DID did = new DID("");
        DIDAccount didAccount = did.create(HdSupport.MnemonicType.CHINESE);


        EthAccount ethAccount = new EthWallet(nodeUrl).exportByMnemonic(didAccount.getMnemonic());
        String privateKey = ethAccount.getPrivateKey();

        EthAccount ethAccount1 = new EthAccount(privateKey);
        System.out.println("ethAccount1 = " + ethAccount1);

        boolean equals = ethAccount1.getAddress().equals(ethAccount.getAddress());
        System.out.println("equals = " + equals);


    }

    @Test
    public void testSOrt(){
        //
//    public static void main(String[] args) {
//        Map<String,String> map = new TreeMap<>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        })  ;
//        for (Action value : Action.values()) {
//            map.put(value.name(),value.name());
//        }
//    }
    }
}

