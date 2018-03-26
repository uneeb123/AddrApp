'use-strict';

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Button,
  FlatList,
  Image
} from 'react-native';
import { Icon } from 'react-native-elements'
import BitcoinWallet from './BitcoinWallet'

class Transaction extends Component<{}> {
  render() {
    return (
      <View style={styles.transactionContainer}>
        <View style={styles.dateContainer}>
          <Text>{this.props.item.date}</Text>
        </View>
        <View style={styles.addrContainer}>
          <Text>{this.props.item.address}</Text>
        </View>
        <View style={styles.priceContainer}>
          <Text>{this.props.item.amount}</Text>
        </View>
      </View>
    );
  }
}

class AccountPanel extends Component {
  render() {
    return (
      <View style={{backgroundColor: 'white', margin: 2, elevation: 2}}>
        <View>
          <Text style={{fontFamily: 'monospace'}}>mBTC</Text>
        </View>
        <View>
          <Text>{this.props.address}</Text>
          <Text>{this.props.balance}</Text>
        </View>
      </View>
    );
  }
}

class TransactionList extends Component<{}> {
  
  _parseData() {
    amounts = this.props.amounts;
    addresses = this.props.addresses;
    dates = this.props.dates;

    var txs = []
    var total_number = amounts.length //all should be same length
    if (total_number == 0) {
      this.txs = [];
    }
    else {
      for (var i = 1; i <= total_number; i++) {
        var tx = {}
        tx.key = i.toString();
        tx.amount = amounts[i-1];
        tx.address = addresses[i-1];
        tx.date = dates[i-1];
        txs.push(tx);
      }
      this.txs = txs;
    }
    console.log('tx: %j', this.txs);
  }

  render() {
    this._parseData();
    return (
      <View style={styles.transactions}>
        <FlatList 
          data={this.txs}
          renderItem={({item}) => <Transaction item={item}/>}
        />
      </View>
    );
  }
}

class LogoTitle extends Component<{}> {
  render() {
    return (
      <Text style={{ fontFamily: 'pacifico', color: 'white', fontSize: 25 }}>
        addr
      </Text>
    );
  }
}

export default class HomePage extends Component<{}> {
  static navigationOptions = ({navigation}) => {
    const params = navigation.state.params || {};
    
    return {
      headerLeft: <View style={{width: 100, height: 100}} />,
      headerTitle: <LogoTitle/>,
      headerRight: (
        <View style={{margin: 10}}>
          <Icon
            onPress={() => navigation.navigate('Info')}
            color="#fff"
            name='info' />
        </View>
      ),
    }
  };

  constructor(props) {
    super(props);
    this.state = {walletReady: false, txsReady: false};
    this._getWallet();
  }

  async _getWallet() {
    try {
      var {address, balance} = await BitcoinWallet.getWallet();
      this.address = address;
      this.balance = balance;
      this.setState(previousState => {
        this._getTransactions();
        previousState.walletReady = true;
        return previousState;
      });
    } catch(e) {
      console.log(e);
    }
  }

  async _getTransactions() {
    try {
      var {amounts, addresses, dates} = await BitcoinWallet.getTransactions();
      this.all_amounts = amounts;
      this.all_addresses = addresses;
      this.all_dates = dates;
      this.setState(previousState => {
        previousState.txsReady = true;
        return previousState;
      });
    } catch(e) {
      console.log(e);
    }
  }

  _recvSubmit = () => {
    let address = this.state.walletReady? this.address: '';
    this.props.navigation.navigate('Receive', {address: address})
  }

  _sendSubmit = () => {
    this.props.navigation.navigate('Send')
  }

  render() {
    let address = this.state.walletReady? this.address: '-';
    let balance = this.state.walletReady? this.balance: '-';
    let tx_amounts = this.state.txsReady? this.all_amounts: [];
    let tx_addresses = this.state.txsReady? this.all_addresses: [];
    let tx_dates = this.state.txsReady? this.all_dates: [];
    return (
      <View style={styles.container}>
        <AccountPanel address={address} balance={balance} />
        <TransactionList amounts={tx_amounts} addresses={tx_addresses} dates={tx_dates}/>
        <View style={styles.bottomButtons}>
          <Button 
            color='#298e82' title='Receive coins' onPress={this._recvSubmit}/>
          <Button 
            color='#298e82' title='Send coins' onPress={this._sendSubmit}/>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'stretch',
    backgroundColor: '#F5FCFF',
    padding: 5,
  },
  transactions: {
    flex: 1,
  },
  bottomButtons: {
    justifyContent: 'space-between',
    flexDirection: 'row',
    margin: 2,
  },
  transactionContainer: {
    flexDirection: 'row',
    backgroundColor: 'white',
    margin: 2,
    elevation: 2,
  },
  dateContainer: {
    margin: 1,
    flex: 1,
  },
  addrContainer: {
    margin: 1,
    flex: 2,
  },
  priceContainer: {
    margin: 1,
    flex: 1,
  },
});
