package org.elastos.dma.base.conf;



public class NodeConstant {


    private static final String URL_VERSION_PREFIX = "/api/v1";

    public static final String CONNECTION_COUNT = URL_VERSION_PREFIX + "/node/connectioncount";
    public static final String STATE = URL_VERSION_PREFIX + "/node/state";
    public static final String BLOCK_TX_BYHEIGHT = URL_VERSION_PREFIX + "/block/transactions/height/";
    public static final String BLOCK_BY_HEIGHT = URL_VERSION_PREFIX + "/block/details/height/";
    public static final String BLOCK_BY_HASH = URL_VERSION_PREFIX + "/block/details/hash/";
    public static final String BLOCK_HEIGHT = URL_VERSION_PREFIX + "/block/height";
    public static final String BLOCK_HASH = URL_VERSION_PREFIX + "/block/hash/";
    public static final String TRANSACTION = URL_VERSION_PREFIX + "/transaction/";
    public static final String ASSET = URL_VERSION_PREFIX + "/asset/";
    public static final String BALANCE_BY_ADDR = URL_VERSION_PREFIX + "/asset/balances/";
    public static final String BALANCE_BY_ASSET = URL_VERSION_PREFIX + "/asset/balance/";
    public static final String UTXO_BY_ASSET = URL_VERSION_PREFIX + "/asset/utxo/";
    public static final String UTXO_BY_ADDR = URL_VERSION_PREFIX + "/asset/utxos/";
    public static final String SEND_RAW_TRANSACTION = URL_VERSION_PREFIX + "/transaction";
    public static final String TRANSACTION_POOL = URL_VERSION_PREFIX + "/transactionpool";
    public static final String RESTART = URL_VERSION_PREFIX + "/restart";

}
