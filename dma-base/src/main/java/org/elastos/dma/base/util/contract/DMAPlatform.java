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
import org.web3j.abi.datatypes.DynamicArray;
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
public class DMAPlatform extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405160a08061184e8339810160409081528151602083015191830151606084015160809094015191939091600160a060020a03851615156100b457604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601660248201527f696e76616c696420657263373231206164647265737300000000000000000000604482015290519081900360640190fd5b600160a060020a038416151561012b57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601560248201527f696e76616c696420657263323020616464726573730000000000000000000000604482015290519081900360640190fd5b600160a060020a0385811690851614156101cc57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152602860248201527f73686f756420646966666972656e74206265747765656e20657263373231206160448201527f6e64206572633230000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b60008054600160a060020a0319908116600160a060020a039788161782556001805482169688169690961790955560028054909516939095169290921790925560039190915560045561162990819061022590396000f3006080604052600436106100745763ffffffff60e060020a60003504166301f10edd8114610079578063095bcdb6146100a257806328b13177146100c95780632e5b411d1461012e578063381c3277146101615780637b167781146101a157806380cc44ce146101b957806381a9d24d1461021e575b600080fd5b34801561008557600080fd5b506100a0600160a060020a0360043516602435604435610273565b005b3480156100ae57600080fd5b506100a0600160a060020a0360043516602435604435610283565b3480156100d557600080fd5b506040805160206004602480358281013584810280870186019097528086526100a0968435600160a060020a03169636966044959194909101929182918501908490808284375094975050933594506102a99350505050565b34801561013a57600080fd5b5061014f600160a060020a03600435166102df565b60408051918252519081900360200190f35b34801561016d57600080fd5b506101796004356102fa565b60408051600160a060020a039094168452602084019290925282820152519081900360600190f35b3480156101ad57600080fd5b506100a06004356103bf565b3480156101c557600080fd5b506040805160206004602480358281013584810280870186019097528086526100a0968435600160a060020a03169636966044959194909101929182918501908490808284375094975050933594506105e19350505050565b34801561022a57600080fd5b50604080516020600480358082013583810280860185019096528085526100a095369593946024949385019291829185019084908082843750949750610e309650505050505050565b61027e838383610f01565b505050565b606061029683600163ffffffff6113c716565b90506102a38482846105e1565b50505050565b60005b82518110156102a3576102d78484838151811015156102c757fe5b9060200190602002015184610f01565b6001016102ac565b600160a060020a031660009081526006602052604090205490565b60008054604080517f6352211e00000000000000000000000000000000000000000000000000000000815260048101859052905183928392600160a060020a0390911691636352211e9160248082019260209290919082900301818787803b15801561036557600080fd5b505af1158015610379573d6000803e3d6000fd5b505050506040513d602081101561038f57600080fd5b5051600160a060020a03811660009081526005602090815260408083208884529091529020549095909350915050565b60008054604080517f6352211e000000000000000000000000000000000000000000000000000000008152600481018590529051600160a060020a0390921691636352211e9160248082019260209290919082900301818787803b15801561042657600080fd5b505af115801561043a573d6000803e3d6000fd5b505050506040513d602081101561045057600080fd5b5051600160a060020a0381166000908152600560209081526040808320868452909152812054919250106104ce576040805160e560020a62461bcd02815260206004820152601160248201527f61737365742073686f7564206578697374000000000000000000000000000000604482015290519081900360640190fd5b600160a060020a038116331461052e576040805160e560020a62461bcd02815260206004820152600d60248201527f4e6f207065726d697373696f6e00000000000000000000000000000000000000604482015290519081900360640190fd5b60008054604080517f7b167781000000000000000000000000000000000000000000000000000000008152600481018690529051600160a060020a0390921692637b1677819260248084019382900301818387803b15801561058f57600080fd5b505af11580156105a3573d6000803e3d6000fd5b5050604051849250600160a060020a03841691507f35b41e25755146f54cf596f9bc88f5b666fb2c139f8a145124fa59b47f50929390600090a35050565b60008060008060008060008060008060008060008e5111151561064e576040805160e560020a62461bcd02815260206004820152601960248201527f61727261792073686f756c64206e6f7420626520656d70747900000000000000604482015290519081900360640190fd5b60009b5060009a50600099505b8d518a1015610a3f578d8a81518110151561067257fe5b9060200190602002015198506000809054906101000a9004600160a060020a0316600160a060020a0316636352211e8a6040518263ffffffff1660e060020a02815260040180828152602001915050602060405180830381600087803b1580156106db57600080fd5b505af11580156106ef573d6000803e3d6000fd5b505050506040513d602081101561070557600080fd5b810190808051906020019092919050505097508e600160a060020a031688600160a060020a0316141515610783576040805160e560020a62461bcd02815260206004820152601c60248201527f617373657274206f776e6572206973206e6f74206d6174636865642e00000000604482015290519081900360640190fd5b600160a060020a03881660009081526005602090815260408083208c8452909152812054116107fc576040805160e560020a62461bcd02815260206004820152601160248201527f61737365742073686f7564206578697374000000000000000000000000000000604482015290519081900360640190fd5b60008054604080517f081812fc000000000000000000000000000000000000000000000000000000008152600481018d90529051600160a060020a039092169263081812fc926024808401936020939083900390910190829087803b15801561086457600080fd5b505af1158015610878573d6000803e3d6000fd5b505050506040513d602081101561088e57600080fd5b50519650600160a060020a03871630146108f2576040805160e560020a62461bcd02815260206004820152601d60248201527f6e6f207065726d697373696f6e20666f722037323120617070726f7665000000604482015290519081900360640190fd5b60008054604080517f42842e0e000000000000000000000000000000000000000000000000000000008152600160a060020a038c81166004830152336024830152604482018e9052915191909216926342842e0e926064808201939182900301818387803b15801561096357600080fd5b505af1158015610977573d6000803e3d6000fd5b50505060008a815260076020526040812054111590506109cd57600160a060020a03881660009081526005602090815260408083208c84529091529020546109c6908c9063ffffffff61143c16565b9a50610a05565b600160a060020a03881660009081526005602090815260408083208c8452909152902054610a02908d9063ffffffff61143c16565b9b505b600089815260076020526040902054610a2590600163ffffffff61143c16565b60008a81526007602052604090205560019099019861065b565b600095506000945060008c1115610a7c57610a628c6103e863ffffffff61145216565b9550610a796003548761146990919063ffffffff16565b95505b60008b1115610ab157610a978b6103e863ffffffff61145216565b9450610aae6004548661146990919063ffffffff16565b94505b600154604080517fdd62ed3e0000000000000000000000000000000000000000000000000000000081523360048201523060248201529051600160a060020a039092169163dd62ed3e916044808201926020929091908290030181600087803b158015610b1d57600080fd5b505af1158015610b31573d6000803e3d6000fd5b505050506040513d6020811015610b4757600080fd5b505193508c841015610ba3576040805160e560020a62461bcd02815260206004820152601160248201527f6e6f20656e6f75676820617070726f7665000000000000000000000000000000604482015290519081900360640190fd5b610bb38c8c63ffffffff61143c16565b925082841015610c0d576040805160e560020a62461bcd02815260206004820152601160248201527f6e6f20656e6f75676820617070726f7665000000000000000000000000000000604482015290519081900360640190fd5b610c1d868663ffffffff61143c16565b9150610c2f838363ffffffff61149416565b600154604080517f23b872dd000000000000000000000000000000000000000000000000000000008152336004820152306024820152604481018790529051929350600160a060020a03909116916323b872dd916064808201926020929091908290030181600087803b158015610ca557600080fd5b505af1158015610cb9573d6000803e3d6000fd5b505050506040513d6020811015610ccf57600080fd5b50506000821115610d7957600154600254604080517fa9059cbb000000000000000000000000000000000000000000000000000000008152600160a060020a039283166004820152602481018690529051919092169163a9059cbb9160448083019260209291908290030181600087803b158015610d4c57600080fd5b505af1158015610d60573d6000803e3d6000fd5b505050506040513d6020811015610d7657600080fd5b50505b600154604080517fa9059cbb000000000000000000000000000000000000000000000000000000008152600160a060020a038b81166004830152602482018590529151919092169163a9059cbb9160448083019260209291908290030181600087803b158015610de857600080fd5b505af1158015610dfc573d6000803e3d6000fd5b505050506040513d6020811015610e1257600080fd5b50610e1f90508f8f6114a6565b505050505050505050505050505050565b600080805b8351831015610ef7578383815181101515610e4c57fe5b9060200190602002015191506000809054906101000a9004600160a060020a0316600160a060020a0316636352211e836040518263ffffffff1660e060020a02815260040180828152602001915050602060405180830381600087803b158015610eb557600080fd5b505af1158015610ec9573d6000803e3d6000fd5b505050506040513d6020811015610edf57600080fd5b50519050610eec826103bf565b600190920191610e35565b6102a381856114a6565b60008080600160a060020a0386161515610f65576040805160e560020a62461bcd02815260206004820152601560248201527f696e76616c6964206f776e657220616464726573730000000000000000000000604482015290519081900360640190fd5b60008411610fbd576040805160e560020a62461bcd02815260206004820152601760248201527f76616c756520636f756c64206d6f7265207468616e2030000000000000000000604482015290519081900360640190fd5b60008511611015576040805160e560020a62461bcd02815260206004820152601a60248201527f746f6b656e49642073686f756c64206d6f7265207468616e2030000000000000604482015290519081900360640190fd5b60008054604080517f6352211e000000000000000000000000000000000000000000000000000000008152600481018990529051600160a060020a0390921692636352211e926024808401936020939083900390910190829087803b15801561107d57600080fd5b505af1158015611091573d6000803e3d6000fd5b505050506040513d60208110156110a757600080fd5b505160008054604080517f081812fc000000000000000000000000000000000000000000000000000000008152600481018a90529051939650600160a060020a039091169263081812fc92602480840193602093929083900390910190829087803b15801561111557600080fd5b505af1158015611129573d6000803e3d6000fd5b505050506040513d602081101561113f57600080fd5b505160008054604080517f6f3de7d7000000000000000000000000000000000000000000000000000000008152600481018a90529051939550600160a060020a0390911692636f3de7d792602480840193602093929083900390910190829087803b1580156111ad57600080fd5b505af11580156111c1573d6000803e3d6000fd5b505050506040513d60208110156111d757600080fd5b50519050600181151514611235576040805160e560020a62461bcd02815260206004820152601860248201527f4173736572742063616e2774206265207472616e736665720000000000000000604482015290519081900360640190fd5b600160a060020a0382163014611295576040805160e560020a62461bcd02815260206004820152601760248201527f696e76616c696420617070726f76652061646472657373000000000000000000604482015290519081900360640190fd5b600160a060020a03838116908716146112f8576040805160e560020a62461bcd02815260206004820152601560248201527f696e76616c696420746f6b656e4964206f776e65720000000000000000000000604482015290519081900360640190fd5b600160a060020a0383163314611358576040805160e560020a62461bcd02815260206004820152600e60248201527f696e76616c69642063616c6c6572000000000000000000000000000000000000604482015290519081900360640190fd5b600160a060020a03861660009081526005602090815260408083208884529091529020849055611387866114e2565b838587600160a060020a03167f72020b040acc0880529e273320aec74a8160fd2b335465b5881635ee1f8a457060405160405180910390a4505050505050565b60606000826040519080825280602002602001820160405280156113f5578160200160208202803883390190505b509150600090505b8281101561143557611415848263ffffffff61143c16565b828281518110151561142357fe5b602090810290910101526001016113fd565b5092915050565b60008282018381101561144b57fe5b9392505050565b600080828481151561146057fe5b04949350505050565b60008083151561147c5760009150611435565b5082820282848281151561148c57fe5b041461144b57fe5b6000828211156114a057fe5b50900390565b6000805b82518210156102a35782828151811015156114c157fe5b9060200190602002015190506114d78482611566565b6001909101906114aa565b600160a060020a03811660009081526006602052604090205461150c90600163ffffffff61143c16565b600160a060020a038216600081815260066020908152604091829020849055815192835282019290925281517f99a0e829f28dc9bf1f2a53a17e98b346966337672872318be177ad08fe4fffc6929181900390910190a150565b600160a060020a038216600090815260056020908152604080832084845290915281205561159382611597565b5050565b600160a060020a038116600090815260066020526040812054116115b757fe5b600160a060020a0381166000908152600660205260409020546115e190600163ffffffff61149416565b600160a060020a039091166000908152600660205260409020555600a165627a7a723058202f5cb32f4b94fddc62325ced80da1aaad0fb1c9088dbb87ff3ab285b2c6d71d70029";

    protected DMAPlatform(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DMAPlatform(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<SaveApproveEventResponse> getSaveApproveEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("SaveApprove", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}),
                Arrays.<TypeReference<?>>asList());
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<SaveApproveEventResponse> responses = new ArrayList<SaveApproveEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SaveApproveEventResponse typedResponse = new SaveApproveEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SaveApproveEventResponse> saveApproveEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("SaveApprove", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SaveApproveEventResponse>() {
            @Override
            public SaveApproveEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                SaveApproveEventResponse typedResponse = new SaveApproveEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<RevokeApproveEventResponse> getRevokeApproveEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("RevokeApprove", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
                Arrays.<TypeReference<?>>asList());
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<RevokeApproveEventResponse> responses = new ArrayList<RevokeApproveEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RevokeApproveEventResponse typedResponse = new RevokeApproveEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RevokeApproveEventResponse> revokeApproveEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("RevokeApprove", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RevokeApproveEventResponse>() {
            @Override
            public RevokeApproveEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                RevokeApproveEventResponse typedResponse = new RevokeApproveEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._array = (List<BigInteger>) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._array = (List<BigInteger>) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<IncCntEventResponse> getIncCntEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("IncCnt", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<IncCntEventResponse> responses = new ArrayList<IncCntEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            IncCntEventResponse typedResponse = new IncCntEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._cnt = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<IncCntEventResponse> incCntEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("IncCnt", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, IncCntEventResponse>() {
            @Override
            public IncCntEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                IncCntEventResponse typedResponse = new IncCntEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._cnt = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> saveApprove(String _owner, BigInteger _tokenId, BigInteger _value) {
        final Function function = new Function(
                "saveApprove", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transfer(String _owner, BigInteger _tokenId, BigInteger _value) {
        final Function function = new Function(
                "transfer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> saveApproveWithArray(String _owner, List<BigInteger> _tokenArr, BigInteger _value) {
        final Function function = new Function(
                "saveApproveWithArray", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(_tokenArr, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getAssetCnt(String _owner) {
        final Function function = new Function("getAssetCnt", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple3<String, BigInteger, BigInteger>> getApproveinfo(BigInteger _tokenId) {
        final Function function = new Function("getApproveinfo", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple3<String, BigInteger, BigInteger>>(
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> revokeApprove(BigInteger _tokenId) {
        final Function function = new Function(
                "revokeApprove", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferWithArray(String _owner, List<BigInteger> _array, BigInteger _value) {
        final Function function = new Function(
                "transferWithArray", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(_array, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> revokeApprovesWithArray(List<BigInteger> _tokenArr) {
        final Function function = new Function(
                "revokeApprovesWithArray", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(_tokenArr, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<DMAPlatform> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, String _platformAddress, BigInteger _firstExpenses, BigInteger _secondExpenses) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721), 
                new org.web3j.abi.datatypes.Address(_token20), 
                new org.web3j.abi.datatypes.Address(_platformAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(_firstExpenses), 
                new org.web3j.abi.datatypes.generated.Uint256(_secondExpenses)));
        return deployRemoteCall(DMAPlatform.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<DMAPlatform> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _token721, String _token20, String _platformAddress, BigInteger _firstExpenses, BigInteger _secondExpenses) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_token721), 
                new org.web3j.abi.datatypes.Address(_token20), 
                new org.web3j.abi.datatypes.Address(_platformAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(_firstExpenses), 
                new org.web3j.abi.datatypes.generated.Uint256(_secondExpenses)));
        return deployRemoteCall(DMAPlatform.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static DMAPlatform load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DMAPlatform(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static DMAPlatform load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DMAPlatform(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class SaveApproveEventResponse {
        public Log log;

        public String _owner;

        public BigInteger _tokenId;

        public BigInteger _value;
    }

    public static class RevokeApproveEventResponse {
        public Log log;

        public String _owner;

        public BigInteger _tokenId;
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public List<BigInteger> _array;

        public BigInteger _value;
    }

    public static class IncCntEventResponse {
        public Log log;

        public String _owner;

        public BigInteger _cnt;
    }
}
