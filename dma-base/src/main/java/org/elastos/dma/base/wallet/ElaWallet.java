package org.elastos.dma.base.wallet;

import org.elastos.dma.base.entity.ela.ElaAccount;
import org.elastos.dma.base.util.ElaRpcImpl;
import org.elastos.dma.base.util.HdSupport;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/10/31 14:39
 */
public class ElaWallet extends ElaRpcImpl {

    public ElaWallet(String nodeUrl) {
        super(nodeUrl);
    }

    /**
     * 创建账号
     *
     * @param mnemonicType 助记词类型
     * @return ElaAccount
     */

    public ElaAccount create(HdSupport.MnemonicType mnemonicType) {
        return ElaAccount.init(mnemonicType);
    }

    /**
     * 创建密码账号
     *
     * @param mnemonicType 助记词类型
     * @param pwd          密码
     * @return ElaAccount
     */

    public ElaAccount create(HdSupport.MnemonicType mnemonicType, String pwd) {
        return ElaAccount.init(mnemonicType,pwd);
    }


    /**
     * 使用私钥导出
     *
     * @param privateKey 私钥
     * @return ElaAccount
     */

    public ElaAccount exportByPrivateKey(String privateKey) {
        return ElaAccount.getElaAccountByPrivateKey(privateKey);
    }


    /**
     * 使用助记词导出
     *
     * @param mnemonic 助记词
     * @return ElaAccount
     */

    public ElaAccount exportByMnemonic(String mnemonic) {
        ElaAccount elaAccount = ElaAccount.getElaAccountByMnemonic(mnemonic);
        return elaAccount;
    }

    /**
     * 使用助记词、密码导出
     *
     * @param mnemonic 助记词
     * @param pwd      密码
     * @return ElaAccount
     */

    public ElaAccount exportByMnemonic(String mnemonic, String pwd) {
        ElaAccount elaAccount = ElaAccount.getElaAccountByMnemonic(mnemonic, pwd);
        return elaAccount;
    }

}
