package org.elastos.dma.base.storage;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/12/25 10:00
 */
public class Ipfs {

    private Ipfs() {
    }

    private volatile static IPFS ipfs;//"/ip4/192.168.1.113/tcp/5001"

    public static Ipfs getClient(String multiaddr) {
        if (ipfs == null) {
            synchronized (Ipfs.class) {
                if (ipfs == null) {
                    ipfs = new IPFS(multiaddr);
                }
            }
        }
        return new Ipfs();
    }


    public String putBytes(byte[] data) throws IOException {
        NamedStreamable.ByteArrayWrapper byteArrayWrapper = new NamedStreamable.ByteArrayWrapper(data);
        List<MerkleNode> add = ipfs.add(byteArrayWrapper);
        MerkleNode addResult = add.get(0);
        return addResult.hash.toString();
    }

    /**
     * 上传文件
     *
     * @param f 文件
     * @return hash
     */
    public String putFile(File f) throws IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(f);
        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toString();
    }

    /**
     * 上传字符串
     *
     * @param str
     * @return hash
     */
    public String putString(String str) throws IOException {
        byte[] data = str.getBytes("utf-8");
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toString();

    }


    /**
     * 根据hash获取字符串
     *
     * @param hash hash值
     * @return json串
     */
    public String getString(String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] data = ipfs.cat(filePointer);
        String str = new String(data, "utf-8");
        return str;
    }

    /**
     * 根据hash获取字节数组
     *
     * @param hash hash值
     * @return 字节数组
     */
    public byte[] getBytes(String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] data = ipfs.cat(filePointer);
        return data;
    }



}
