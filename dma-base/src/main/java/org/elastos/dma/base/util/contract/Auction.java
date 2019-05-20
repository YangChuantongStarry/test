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
import org.web3j.tuples.generated.Tuple4;
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
public class Auction extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405160c080610b4e83398101604090815281516020830151918301516060840151608085015160a09095015160008054600160a060020a0319908116331790915560018054600160a060020a03968716908316179055600280549590961694169390931790935560035560048290556005929092556006919091556008556009805460ff19169055610aa5806100a96000396000f30060806040526004361061008d5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633343414b8114610092578063454a2ab3146100c057806346339493146100da5780637df6a6c8146100ef5780638da5cb5b14610107578063d2f7265a14610138578063f2fde38b1461014d578063f5e234471461016e575b600080fd5b34801561009e57600080fd5b506100a76101b3565b6040805192835260208301919091528051918290030190f35b3480156100cc57600080fd5b506100d86004356101ba565b005b3480156100e657600080fd5b506100d86102c7565b3480156100fb57600080fd5b506100d860043561035b565b34801561011357600080fd5b5061011c610377565b60408051600160a060020a039092168252519081900360200190f35b34801561014457600080fd5b506100d8610386565b34801561015957600080fd5b506100d8600160a060020a0360043516610442565b34801561017a57600080fd5b506101836104d6565b60408051948552600160a060020a0390931660208501528383019190915215156060830152519081900360800190f35b6006544291565b8060065442111580156101ce575060085481115b80156101dd575060095460ff16155b1515610259576040805160e560020a62461bcd02815260206004820152603560248201527f697427732066696e6973686564206f722062696420766c617565206973206c6560448201527f7373207468616e2063757272656e742076616c75650000000000000000000000606482015290519081900360840190fd5b600754600160a060020a03161580159061027e5750600754600160a060020a03163314155b156102985760075461029890600160a060020a03166104fa565b6102a233836105a8565b60006005541180156102b657506005548210155b156102c3576102c361079a565b5050565b600754600160a060020a0316331415610350576040805160e560020a62461bcd02815260206004820152602360248201527f63757272656e74206164647265737320636f756c64206e6f742063616c6c207460448201527f6869730000000000000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b610359336104fa565b565b600054600160a060020a0316331461037257600080fd5b600655565b600054600160a060020a031681565b60065442116103df576040805160e560020a62461bcd02815260206004820152601660248201527f656e642d74696d65206973206e6f742065786365656400000000000000000000604482015290519081900360640190fd5b60095460ff161561043a576040805160e560020a62461bcd02815260206004820152601260248201527f656e642d74696d65206973206578636565640000000000000000000000000000604482015290519081900360640190fd5b61035961079a565b600054600160a060020a0316331461045957600080fd5b600160a060020a038116151561046e57600080fd5b60008054604051600160a060020a03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a36000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b6008546007546006546009549293600160a060020a0390921692909160ff90911690565b600060085411156105a557600254600854604080517fa9059cbb000000000000000000000000000000000000000000000000000000008152600160a060020a03858116600483015260248201939093529051919092169163a9059cbb9160448083019260209291908290030181600087803b15801561057857600080fd5b505af115801561058c573d6000803e3d6000fd5b505050506040513d60208110156105a257600080fd5b50505b50565b600254604080517fdd62ed3e000000000000000000000000000000000000000000000000000000008152600160a060020a0385811660048301523060248301529151600093929092169163dd62ed3e9160448082019260209290919082900301818787803b15801561061957600080fd5b505af115801561062d573d6000803e3d6000fd5b505050506040513d602081101561064357600080fd5b50519050818110156106c5576040805160e560020a62461bcd02815260206004820152602960248201527f617070726f76652076616c7565206f66206e657720616464657273732069736e60448201527f277420656e6f7567680000000000000000000000000000000000000000000000606482015290519081900360840190fd5b600254604080517f23b872dd000000000000000000000000000000000000000000000000000000008152600160a060020a03868116600483015230602483015260448201869052915191909216916323b872dd9160648083019260209291908290030181600087803b15801561073a57600080fd5b505af115801561074e573d6000803e3d6000fd5b505050506040513d602081101561076457600080fd5b50506007805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03949094169390931790925560085550565b600154600354604080517f6352211e0000000000000000000000000000000000000000000000000000000081526004810192909252516000928392600160a060020a0390911691636352211e9160248082019260209290919082900301818787803b15801561080857600080fd5b505af115801561081c573d6000803e3d6000fd5b505050506040513d602081101561083257600080fd5b5051600154600354604080517f081812fc000000000000000000000000000000000000000000000000000000008152600481019290925251929450600160a060020a039091169163081812fc916024808201926020929091908290030181600087803b1580156108a157600080fd5b505af11580156108b5573d6000803e3d6000fd5b505050506040513d60208110156108cb57600080fd5b50519050600160a060020a038116301461092f576040805160e560020a62461bcd02815260206004820152601760248201527f696e76616c696420617070726f76652061646472657373000000000000000000604482015290519081900360640190fd5b600254600854604080517f23b872dd000000000000000000000000000000000000000000000000000000008152306004820152600160a060020a0386811660248301526044820193909352905191909216916323b872dd9160648083019260209291908290030181600087803b1580156109a857600080fd5b505af11580156109bc573d6000803e3d6000fd5b505050506040513d60208110156109d257600080fd5b5050600154600754600354604080517f42842e0e000000000000000000000000000000000000000000000000000000008152600160a060020a038781166004830152938416602482015260448101929092525191909216916342842e0e91606480830192600092919082900301818387803b158015610a5057600080fd5b505af1158015610a64573d6000803e3d6000fd5b50506009805460ff19166001179055505050505600a165627a7a72305820a4d207491eca34ff1ecc7101fc4dbd9eb7f9c1fcf500fdae6798e6d65394e0260029";

    protected Auction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Auction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
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

    public RemoteCall<TransactionReceipt> bid(BigInteger _bidValue) {
        final Function function = new Function(
                "bid",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_bidValue)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> revokeToken() {
        final Function function = new Function(
                "revokeToken",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setEndTimestamp(BigInteger _newTimestamp) {
        final Function function = new Function(
                "setEndTimestamp",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_newTimestamp)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> exchange() {
        final Function function = new Function(
                "exchange",
                Arrays.<Type>asList(),
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

    public RemoteCall<Tuple4<BigInteger, String, BigInteger, Boolean>> getBidInfo() {
        final Function function = new Function("getBidInfo",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple4<BigInteger, String, BigInteger, Boolean>>(
                new Callable<Tuple4<BigInteger, String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple4<BigInteger, String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, String, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (Boolean) results.get(3).getValue());
                    }
                });
    }

    public static RemoteCall<Auction> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, BigInteger _tokenId, BigInteger _lowestValue, BigInteger _closingValue, BigInteger _endTimestamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721),
                new org.web3j.abi.datatypes.Address(_token20),
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_lowestValue),
                new org.web3j.abi.datatypes.generated.Uint256(_closingValue),
                new org.web3j.abi.datatypes.generated.Uint256(_endTimestamp)));
        return deployRemoteCall(Auction.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Auction> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, BigInteger _tokenId, BigInteger _lowestValue, BigInteger _closingValue, BigInteger _endTimestamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721),
                new org.web3j.abi.datatypes.Address(_token20),
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_lowestValue),
                new org.web3j.abi.datatypes.generated.Uint256(_closingValue),
                new org.web3j.abi.datatypes.generated.Uint256(_endTimestamp)));
        return deployRemoteCall(Auction.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Auction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Auction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Auction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Auction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }
}
