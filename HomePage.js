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
          <Text>{this.props.item.addr}</Text>
        </View>
        <View style={styles.priceContainer}>
          <Text>{this.props.item.amount_bitcoin + "\n"}</Text>
          <Text>{"$" + this.props.item.amount_dollar}</Text>
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
  render() {
    return (
      <View style={styles.transactions}>
        <FlatList 
          data={[
            {
              key: '1',
              date: '03/13/2018',
              addr: '1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2',
              amount_bitcoin: '0.05',
              amount_dollar: '80.0',
              incoming: true
            },
            {
              key: '2',
              date: '03/16/2018',
              addr: '3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy',
              amount_bitcoin: '0.02',
              amount_dollar: '35.0',
              incoming: false
            }
          ]}
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
    this.state = {walletReady: false};
    this._getWallet();
  }

  async _getWallet() {
    try {
      var {address, balance} = await BitcoinWallet.getWallet();
      this.address = address;
      this.balance = balance;
      this.setState(previousState => {
        return { walletReady: true };
      });
    } catch(e) {
      console.log(e);
    }
  }

  _recvSubmit = () => {
    this.props.navigation.navigate('Receive')
  }

  _sendSubmit = () => {
    this.props.navigation.navigate('Send')
  }

  render() {
    let address = this.state.walletReady? this.address: '-';
    let balance = this.state.walletReady? this.balance: '-';
    return (
      <View style={styles.container}>
        <AccountPanel address={address} balance={balance} />
        <TransactionList/>
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
