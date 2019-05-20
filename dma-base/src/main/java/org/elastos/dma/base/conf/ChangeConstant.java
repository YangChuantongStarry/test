package org.elastos.dma.base.conf;

import io.github.novacrypto.bip44.BIP44;
import io.github.novacrypto.bip44.Change;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/4/4 17:21
 */
public class ChangeConstant {

    public static final Change ETH_CHANGE = BIP44.m().purpose44().coinType(60).account(0).external();
    public static final Change ELA_CHANGE = BIP44.m().purpose44().coinType(0).account(0).external();
}
