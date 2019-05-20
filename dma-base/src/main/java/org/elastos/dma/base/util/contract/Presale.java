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
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;
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
public class Presale extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405160608061190683398101604090815281516020830151919092015160008054600160a060020a0319908116331790915560018054600160a060020a039586169083161790556002805494909316931692909217905560035561188b8061007b6000396000f3006080604052600436106100b95763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416632261958281146100be5780632af67d59146100db5780633343414b14610114578063519d3908146101425780635af36e3e1461015a5780637df6a6c81461017557806382510bcf1461018d5780638da5cb5b146101ec5780638fbc76421461021d578063e8cfc83b146102e2578063f2fde38b1461030c578063f4accda51461032d575b600080fd5b3480156100ca57600080fd5b506100d9600435602435610354565b005b3480156100e757600080fd5b506100d960048035600160a060020a031690602480359160443591606435916084359182019101356104cb565b34801561012057600080fd5b506101296106bf565b6040805192835260208301919091528051918290030190f35b34801561014e57600080fd5b506100d96004356106c6565b34801561016657600080fd5b506100d960043560243561072c565b34801561018157600080fd5b506100d960043561082c565b34801561019957600080fd5b506101b1600160a060020a0360043516602435610848565b60408051600160a060020a03968716815260208101959095528481019390935293166060830152608082019290925290519081900360a00190f35b3480156101f857600080fd5b50610201610887565b60408051600160a060020a039092168252519081900360200190f35b34801561022957600080fd5b50610235600435610896565b6040518087600160a060020a0316600160a060020a0316815260200186815260200185815260200184815260200180602001838152602001828103825284818151815260200191508051906020019080838360005b838110156102a257818101518382015260200161028a565b50505050905090810190601f1680156102cf5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b3480156102ee57600080fd5b506102fa6004356109e5565b60408051918252519081900360200190f35b34801561031857600080fd5b506100d9600160a060020a03600435166109f7565b34801561033957600080fd5b506100d9600435602435600160a060020a0360443516610a8b565b60606000806000600354421115156103b6576040805160e560020a62461bcd02815260206004820152601660248201527f656e642d74696d65206973206e6f742065786365656400000000000000000000604482015290519081900360640190fd5b6000851161040e576040805160e560020a62461bcd02815260206004820152600a60248201527f656d707479206c6f6f7000000000000000000000000000000000000000000000604482015290519081900360640190fd5b6000868152600860209081526040918290208054835181840281018401909452808452909183018282801561046c57602002820191906000526020600020905b8154600160a060020a0316815260019091019060200180831161044e575b505050505093508351851115610483578351610485565b845b9250600091505b828210156104c35783828151811015156104a257fe5b9060200190602002015190506104b88682610f1c565b60019091019061048c565b505050505050565b600054600160a060020a031633146104e257600080fd5b600160a060020a038616158015906104fa5750600085115b80156105065750600084115b80156105125750600083115b151561058e576040805160e560020a62461bcd02815260206004820152602a60248201527f746f6b656e49642f616d6f756e742f76616c756520616c6c2073686f7564206d60448201527f6f7265207468616e203000000000000000000000000000000000000000000000606482015290519081900360840190fd5b600085815260046020526040902060010154156105f5576040805160e560020a62461bcd02815260206004820152601660248201527f417373657420616c726561647920726567697374656400000000000000000000604482015290519081900360640190fd5b60806040519081016040528087600160a060020a0316815260200185815260200184815260200183838080601f016020809104026020016040519081016040528093929190818152602001838380828437505050929093525050506000868152600460209081526040918290208351815473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909116178155838201516001820155918301516002830155606083015180516106b4926003850192019061173e565b505050505050505050565b6003544291565b600354421161071f576040805160e560020a62461bcd02815260206004820152601660248201527f656e642d74696d65206973206e6f742065786365656400000000000000000000604482015290519081900360640190fd5b6107298133610f1c565b50565b600354421115610786576040805160e560020a62461bcd02815260206004820152601260248201527f656e642d74696d65206973206578636565640000000000000000000000000000604482015290519081900360640190fd5b6000821180156107965750600081115b15156107ec576040805160e560020a62461bcd02815260206004820152601960248201527f696e76616c696420726566756e6420706172616d657465727300000000000000604482015290519081900360640190fd5b6107f733838361134a565b600082815260056020526040902054610816908263ffffffff6115d316565b6000928352600560205260409092209190915550565b600054600160a060020a0316331461084357600080fd5b600355565b600160a060020a038281166000908152600760209081526040808320858452909152902080546001820154600290920154949593949093919092169190565b600054600160a060020a031681565b60008181526004602052604081206001015481908190819060609082901515610909576040805160e560020a62461bcd02815260206004820152601260248201527f4173736574206e6f742072656769737465640000000000000000000000000000604482015290519081900360640190fd5b60008781526004602090815260409182902080546001808301546002808501546003909501805488516101009582161595909502600019011691909104601f8101879004870284018701909752868352600160a060020a039093169b508c9a509850919650909290918301828280156109c35780601f10610998576101008083540402835291602001916109c3565b820191906000526020600020905b8154815290600101906020018083116109a657829003601f168201915b5050506000998a52505060056020526040909720549597949693959294915050565b60009081526005602052604090205490565b600054600160a060020a03163314610a0e57600080fd5b600160a060020a0381161515610a2357600080fd5b60008054604051600160a060020a03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a36000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b60008084846003544211158015610ace5750600082815260046020908152604080832060010154600590925290912054610acb908363ffffffff6115e516565b11155b1515610b4a576040805160e560020a62461bcd02815260206004820152602481018290527f69742073686f756c64206e6f742065786365656420656e642d74696d6520616e60448201527f642061737365727420616d6f756e74206c696d6974206e6f7420657863656564606482015290519081900360840190fd5b600087118015610b5a5750600086115b8015610b6e5750600160a060020a03851615155b1515610bc4576040805160e560020a62461bcd02815260206004820152601860248201527f696e76616c6965206f7264657220706172616d65746572730000000000000000604482015290519081900360640190fd5b60008781526004602052604081206001015411610c2b576040805160e560020a62461bcd02815260206004820152601560248201527f617373657274206e6f7420726567697374657265640000000000000000000000604482015290519081900360640190fd5b3360009081526007602090815260408083208a845290915290205415610c9b576040805160e560020a62461bcd02815260206004820152601960248201527f63616e277420737570706f7274206d756c7469206f7264657200000000000000604482015290519081900360640190fd5b600254604080517fdd62ed3e0000000000000000000000000000000000000000000000000000000081523360048201523060248201529051600160a060020a039092169163dd62ed3e916044808201926020929091908290030181600087803b158015610d0757600080fd5b505af1158015610d1b573d6000803e3d6000fd5b505050506040513d6020811015610d3157600080fd5b5051600088815260046020526040902060020154909450610d5990879063ffffffff6115ff16565b925082841015610db3576040805160e560020a62461bcd02815260206004820152601a60248201527f667265657a652076616c7565206973206e6f7420656e6f756768000000000000604482015290519081900360640190fd5b600254604080517f23b872dd000000000000000000000000000000000000000000000000000000008152336004820152306024820152604481018690529051600160a060020a03909216916323b872dd916064808201926020929091908290030181600087803b158015610e2657600080fd5b505af1158015610e3a573d6000803e3d6000fd5b505050506040513d6020811015610e5057600080fd5b505060408051606081018252878152600160a060020a038088166020838101918252838501888152336000818152600784528781208f82528452878120965187559351600180880180549290971673ffffffffffffffffffffffffffffffffffffffff1992831617909655915160029096019590955560088252858320805494850181558352818320909301805490931690931790915589815260059091522054610f01908763ffffffff6115e516565b60009788526005602052604090972096909655505050505050565b610f246117bc565b610f2c6117e7565b6000600160a060020a0384161515610f8e576040805160e560020a62461bcd02815260206004820152600f60248201527f696e76616c696420616464726573730000000000000000000000000000000000604482015290519081900360640190fd5b600160a060020a038085166000908152600760209081526040808320898452825280832081516060810183528154808252600183015490961693810193909352600201549082015294501061102d576040805160e560020a62461bcd02815260206004820152601960248201527f636f756c64206e6f742066696e64206f7264657220696e666f00000000000000604482015290519081900360640190fd5b60008581526004602090815260409182902082516080810184528154600160a060020a03168152600180830154828501526002808401548387015260038401805487516101009482161594909402600019011691909104601f8101869004860283018601909652858252919492936060860193919291908301828280156110f55780601f106110ca576101008083540402835291602001916110f5565b820191906000526020600020905b8154815290600101906020018083116110d857829003601f168201915b50505050508152505091506000826020015111151561115e576040805160e560020a62461bcd02815260206004820152601960248201527f636f756c64206e6f742066696e6420617373657420696e666f00000000000000604482015290519081900360640190fd5b604082015183516111749163ffffffff6115ff16565b60018054602080870151875160608801516040517fa64c04d8000000000000000000000000000000000000000000000000000000008152600160a060020a0384811660048301908152602483018f9052604483018590526084830189905260a4830189905260c060648401908152845160c485015284519a9b50919097169863a64c04d89895978f9795969495859491939260e4909201919087019080838360005b8381101561122e578181015183820152602001611216565b50505050905090810190601f16801561125b5780820380516001836020036101000a031916815260200191505b50975050505050505050600060405180830381600087803b15801561127f57600080fd5b505af1158015611293573d6000803e3d6000fd5b50506002548451604080517fa9059cbb000000000000000000000000000000000000000000000000000000008152600160a060020a03928316600482015260248101879052905191909216935063a9059cbb925060448083019260209291908290030181600087803b15801561130857600080fd5b505af115801561131c573d6000803e3d6000fd5b505050506040513d602081101561133257600080fd5b50508251611343908590879061134a565b5050505050565b600160a060020a03831660009081526007602090815260408083208584529091528120548211156113eb576040805160e560020a62461bcd02815260206004820152602160248201527f726566756e6420616d6f756e74206973206d6f7265207468616e20657869737460448201527f7300000000000000000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b600160a060020a038416600090815260076020908152604080832086845290915290205461141f908363ffffffff6115d316565b600160a060020a038516600090815260076020908152604080832087845282528083209390935560049052206002015461146090839063ffffffff6115ff16565b600160a060020a038516600090815260076020908152604080832087845290915290206002015490915061149a908263ffffffff6115d316565b600160a060020a0380861660009081526007602090815260408083208884528252808320600290810195909555935484517fa9059cbb00000000000000000000000000000000000000000000000000000000815233600482015260248101879052945193169363a9059cbb9360448083019491928390030190829087803b15801561152457600080fd5b505af1158015611538573d6000803e3d6000fd5b505050506040513d602081101561154e57600080fd5b5050600160a060020a038416600090815260076020908152604080832086845290915290205415156115cd57600160a060020a0384166000908152600760209081526040808320868452909152812081815560018101805473ffffffffffffffffffffffffffffffffffffffff19169055600201556115cd838561162a565b50505050565b6000828211156115df57fe5b50900390565b6000828201838110156115f457fe5b8091505b5092915050565b60008083151561161257600091506115f8565b5082820282848281151561162257fe5b04146115f457fe5b600082815260086020526040812054815b818110156116945760008581526008602052604090208054600160a060020a03861691908390811061166957fe5b600091825260209091200154600160a060020a0316141561168c57809250611694565b60010161163b565b6001820383101561171f576000858152600860205260409020805460001984019081106116bd57fe5b6000918252602080832090910154878352600890915260409091208054600160a060020a0390921691859081106116f057fe5b9060005260206000200160006101000a815481600160a060020a030219169083600160a060020a031602179055505b60008581526008602052604090208054906104c3906000198301611819565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061177f57805160ff19168380011785556117ac565b828001600101855582156117ac579182015b828111156117ac578251825591602001919060010190611791565b506117b8929150611842565b5090565b606060405190810160405280600081526020016000600160a060020a03168152602001600081525090565b6080604051908101604052806000600160a060020a031681526020016000815260200160008152602001606081525090565b81548183558181111561183d5760008381526020902061183d918101908301611842565b505050565b61185c91905b808211156117b85760008155600101611848565b905600a165627a7a72305820550bf61999e2fb2a69dee4f57e5018846849c6d104fc0dae0e71b2894509c7400029";

    protected Presale(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Presale(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
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

    public RemoteCall<TransactionReceipt> mintByPlatform(BigInteger _tokenId, BigInteger _count) {
        final Function function = new Function(
                "mintByPlatform",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> registAsset(String _owner, BigInteger _tokenId, BigInteger _amount, BigInteger _value, String _uri) {
        final Function function = new Function(
                "registAsset",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_amount),
                new org.web3j.abi.datatypes.generated.Uint256(_value),
                new org.web3j.abi.datatypes.Utf8String(_uri)),
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

    public RemoteCall<TransactionReceipt> mintByCustomer(BigInteger _tokenId) {
        final Function function = new Function(
                "mintByCustomer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> refund(BigInteger _tokenId, BigInteger _amount) {
        final Function function = new Function(
                "refund",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_amount)),
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

    public RemoteCall<Tuple5<String, BigInteger, BigInteger, String, BigInteger>> getOrderInfo(String _payAddress, BigInteger _tokenId) {
        final Function function = new Function("getOrderInfo",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_payAddress),
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple5<String, BigInteger, BigInteger, String, BigInteger>>(
                new Callable<Tuple5<String, BigInteger, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple5<String, BigInteger, BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, BigInteger, BigInteger, String, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (String) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple6<String, BigInteger, BigInteger, BigInteger, String, BigInteger>> getRegisterInfo(BigInteger _tokenId) {
        final Function function = new Function("getRegisterInfo",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple6<String, BigInteger, BigInteger, BigInteger, String, BigInteger>>(
                new Callable<Tuple6<String, BigInteger, BigInteger, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple6<String, BigInteger, BigInteger, BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, BigInteger, BigInteger, BigInteger, String, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue(),
                                (String) results.get(4).getValue(),
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> getOrderCount(BigInteger _tokenId) {
        final Function function = new Function("getOrderCount",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String _newOwner) {
        final Function function = new Function(
                "transferOwnership",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> order(BigInteger _tokenId, BigInteger _amount, String _receiveAddress) {
        final Function function = new Function(
                "order",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                new org.web3j.abi.datatypes.generated.Uint256(_amount),
                new org.web3j.abi.datatypes.Address(_receiveAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Presale> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, BigInteger _endTimestamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721),
                new org.web3j.abi.datatypes.Address(_token20),
                new org.web3j.abi.datatypes.generated.Uint256(_endTimestamp)));
        return deployRemoteCall(Presale.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Presale> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, BigInteger _endTimestamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721),
                new org.web3j.abi.datatypes.Address(_token20),
                new org.web3j.abi.datatypes.generated.Uint256(_endTimestamp)));
        return deployRemoteCall(Presale.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Presale load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Presale(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Presale load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Presale(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }
}
