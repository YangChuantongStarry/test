package org.elastos.dma.base.util.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class Nlucky extends Contract {
    private static final String BINARY = "6080604052600560095534801561001557600080fd5b5060405160c08061115483398101604090815281516020830151918301516060840151608085015160a09095015160008054600160a060020a0319908116331790915560018054600160a060020a039687169083161790556002805495909616941693909317909355600355600491909155600591909155600a55600b805462ffffff191690556110a9806100ab6000396000f30060806040526004361061008d5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166311610c2581146100925780633343414b146100a95780637df6a6c8146100d7578063888ea243146100ef5780638da5cb5b14610107578063cfe7b77014610138578063e6eec67e14610174578063f2fde38b1461018c575b600080fd5b34801561009e57600080fd5b506100a76101ad565b005b3480156100b557600080fd5b506100be610485565b6040805192835260208301919091528051918290030190f35b3480156100e357600080fd5b506100a760043561048c565b3480156100fb57600080fd5b506100a76004356104a8565b34801561011357600080fd5b5061011c6106c4565b60408051600160a060020a039092168252519081900360200190f35b34801561014457600080fd5b5061014d6106d3565b604080519384529115156020840152600160a060020a031682820152519081900360600190f35b34801561018057600080fd5b506100a76004356106f4565b34801561019857600080fd5b506100a7600160a060020a0360043516610954565b6000600a5442111580156101c45750600b5460ff16155b80156101d85750600b54610100900460ff16155b1515610254576040805160e560020a62461bcd02815260206004820152603560248201527f697427732066696e6973686564206f722062696420766c617565206973206c6560448201527f7373207468616e2063757272656e742076616c75650000000000000000000000606482015290519081900360840190fd5b600254604080517fdd62ed3e0000000000000000000000000000000000000000000000000000000081523360048201523060248201529051600160a060020a039092169163dd62ed3e916044808201926020929091908290030181600087803b1580156102c057600080fd5b505af11580156102d4573d6000803e3d6000fd5b505050506040513d60208110156102ea57600080fd5b505160055490915081101561036f576040805160e560020a62461bcd02815260206004820152602860248201527f667265657a652076616c7565206f66206e657720616464657273732069736e2760448201527f7420656e6f756768000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b600254600554604080517f23b872dd000000000000000000000000000000000000000000000000000000008152336004820152306024820152604481019290925251600160a060020a03909216916323b872dd916064808201926020929091908290030181600087803b1580156103e557600080fd5b505af11580156103f9573d6000803e3d6000fd5b505050506040513d602081101561040f57600080fd5b50506006805460018101825560008290527ff652222313e28459528d920b65115c16c04f3efc82aaedc97be59f3f377c0d3f01805473ffffffffffffffffffffffffffffffffffffffff19163317905560045490541061048257600b805460ff19166001179055600954610482906104a8565b50565b600a544291565b600054600160a060020a031633146104a357600080fd5b600a55565b600b5460009060609082908190610100900460ff1615610512576040805160e560020a62461bcd02815260206004820152601260248201527f656e642d74696d65206973206578636565640000000000000000000000000000604482015290519081900360640190fd5b6000851161056a576040805160e560020a62461bcd02815260206004820152601d60248201527f6c6f6f7020636f756e742073686f756c64206d6f7265207468616e2030000000604482015290519081900360640190fd5b600b5460ff1615156001146105c9576040805160e560020a62461bcd02815260206004820152601860248201527f6c61636b20656e6f756768207061727469636970616e74730000000000000000604482015290519081900360640190fd5b600b5462010000900460ff1615156105e3576105e36109e8565b60065485106105f4576006546105f6565b845b935083604051908082528060200260200182016040528015610622578160200160208202803883390190505b509250600091505b8382101561068857600680548390811061064057fe5b6000918252602090912001548351600160a060020a039091169084908490811061066657fe5b600160a060020a0390921660209283029091019091015260019091019061062a565b5060005b82518110156106bd576106b583828151811015156106a657fe5b90602001906020020151610dbb565b60010161068c565b5050505050565b600054600160a060020a031681565b600654600b54600854919261010090910460ff1691600160a060020a031690565b60006060600080600a5442111515610756576040805160e560020a62461bcd02815260206004820152601660248201527f656e642d74696d65206973206e6f742065786365656400000000000000000000604482015290519081900360640190fd5b600b5460ff16156107b1576040805160e560020a62461bcd02815260206004820152601760248201527f6e6f7420656e6f756768207061727469636970616e7473000000000000000000604482015290519081900360640190fd5b600b54610100900460ff1615610811576040805160e560020a62461bcd02815260206004820152601260248201527f656e642d74696d65206973206578636565640000000000000000000000000000604482015290519081900360640190fd5b60008511610869576040805160e560020a62461bcd02815260206004820152601d60248201527f6c6f6f7020636f756e742073686f756c64206d6f7265207468616e2030000000604482015290519081900360640190fd5b600654851061087a5760065461087c565b845b9350836040519080825280602002602001820160405280156108a8578160200160208202803883390190505b509250600091505b8382101561090e5760068054839081106108c657fe5b6000918252602090912001548351600160a060020a03909116908490849081106108ec57fe5b600160a060020a039092166020928302909101909101526001909101906108b0565b5060005b82518110156106bd5761093b838281518110151561092c57fe5b90602001906020020151610eb4565b61094c83828151811015156106a657fe5b600101610912565b600054600160a060020a0316331461096b57600080fd5b600160a060020a038116151561098057600080fd5b60008054604051600160a060020a03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a36000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b6006546000806109f783610f5c565b915060008210158015610a0957508282105b1515610a5f576040805160e560020a62461bcd02815260206004820152601460248201527f696e76616c69646520617272617920696e646578000000000000000000000000604482015290519081900360640190fd5b6006805483908110610a6d57fe5b60009182526020808320909101546008805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03928316179055600154600354604080517f6352211e0000000000000000000000000000000000000000000000000000000081526004810192909252519190921693636352211e93602480850194919392918390030190829087803b158015610b0557600080fd5b505af1158015610b19573d6000803e3d6000fd5b505050506040513d6020811015610b2f57600080fd5b50516007805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03928316179055600154600354604080517f081812fc000000000000000000000000000000000000000000000000000000008152600481019290925251919092169163081812fc9160248083019260209291908290030181600087803b158015610bbc57600080fd5b505af1158015610bd0573d6000803e3d6000fd5b505050506040513d6020811015610be657600080fd5b50519050600160a060020a0381163014610c4a576040805160e560020a62461bcd02815260206004820152601760248201527f696e76616c696420617070726f76652061646472657373000000000000000000604482015290519081900360640190fd5b600154600754600854600354604080517f42842e0e000000000000000000000000000000000000000000000000000000008152600160a060020a039485166004820152928416602484015260448301919091525191909216916342842e0e91606480830192600092919082900301818387803b158015610cc957600080fd5b505af1158015610cdd573d6000803e3d6000fd5b5050600254600754600554600160a060020a0392831694506323b872dd9350309290911690610d0c9088611007565b604080517c010000000000000000000000000000000000000000000000000000000063ffffffff8716028152600160a060020a0394851660048201529290931660248301526044820152905160648083019260209291908290030181600087803b158015610d7957600080fd5b505af1158015610d8d573d6000803e3d6000fd5b505050506040513d6020811015610da357600080fd5b5050600b805462ff0000191662010000179055505050565b600654600090815b81811015610e0f5783600160a060020a0316600682815481101515610de457fe5b600091825260209091200154600160a060020a03161415610e0757809250610e0f565b600101610dc3565b60018203831015610e8157600680546000198401908110610e2c57fe5b60009182526020909120015460068054600160a060020a039092169185908110610e5257fe5b9060005260206000200160006101000a815481600160a060020a030219169083600160a060020a031602179055505b6006805490610e9490600019830161103d565b506006541515610eae57600b805461ff0019166101001790555b50505050565b600254600554604080517f23b872dd000000000000000000000000000000000000000000000000000000008152306004820152600160a060020a0385811660248301526044820193909352905191909216916323b872dd9160648083019260209291908290030181600087803b158015610f2d57600080fd5b505af1158015610f41573d6000803e3d6000fd5b505050506040513d6020811015610f5757600080fd5b505050565b604080514260208083018290526c010000000000000000000000003302838501526054808401929092528351808403909201825260749092019283905280516000938593909182918401908083835b60208310610fca5780518252601f199092019160209182019101610fab565b5181516020939093036101000a600019018019909116921691909117905260405192018290039091209250505081151561100057fe5b0692915050565b60008083151561101a5760009150611036565b5082820282848281151561102a57fe5b041461103257fe5b8091505b5092915050565b815481835581811115610f5757600083815260209020610f5791810190830161107a91905b808211156110765760008155600101611062565b5090565b905600a165627a7a7230582027d007de60ab78d6fd160eaf9ae66475c4ea5163d8974e1d3328beb0671ad7280029";

    protected Nlucky(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Nlucky(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnershipTransferred",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnershipTransferred",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> bet() {
        final Function function = new Function(
                "bet",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple2<BigInteger, BigInteger>> getEndTimeStamp() {
        final Function function = new Function("getEndTimeStamp",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple2<BigInteger, BigInteger>>(
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> setEndTimestamp(BigInteger _newTimestamp) {
        final Function function = new Function(
                "setEndTimestamp",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_newTimestamp)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> success(BigInteger _count) {
        final Function function = new Function(
                "success",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple3<BigInteger, Boolean, String>> getBetInfo() {
        final Function function = new Function("getBetInfo",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}));
        return new RemoteCall<Tuple3<BigInteger, Boolean, String>>(
                new Callable<Tuple3<BigInteger, Boolean, String>>() {
                    @Override
                    public Tuple3<BigInteger, Boolean, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, Boolean, String>(
                                (BigInteger) results.get(0).getValue(),
                                (Boolean) results.get(1).getValue(),
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> fails(BigInteger _count) {
        final Function function = new Function(
                "fails",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String _newOwner) {
        final Function function = new Function(
                "transferOwnership",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Nlucky> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, BigInteger _tokenId, BigInteger _totalPortions, BigInteger _price, BigInteger _endTimestamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721),
                new org.web3j.abi.datatypes.Address(_token20),
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_totalPortions),
                new org.web3j.abi.datatypes.generated.Uint256(_price),
                new org.web3j.abi.datatypes.generated.Uint256(_endTimestamp)));
        return deployRemoteCall(Nlucky.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Nlucky> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, BigInteger _tokenId, BigInteger _totalPortions, BigInteger _price, BigInteger _endTimestamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721),
                new org.web3j.abi.datatypes.Address(_token20),
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_totalPortions),
                new org.web3j.abi.datatypes.generated.Uint256(_price),
                new org.web3j.abi.datatypes.generated.Uint256(_endTimestamp)));
        return deployRemoteCall(Nlucky.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Nlucky load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Nlucky(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Nlucky load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Nlucky(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }
}
