'use-strict';

import React, { Component } from 'react';
import {
  View,
  TextInput,
  Button
} from 'react-native';
import BitcoinWallet from './BitcoinWallet'

export default class SendPage extends Component<{}> {
  static navigationOptions = {
    title: 'Send coins',
  };

  constructor(props) {
    super(props);
    this.state = { amount: '0.00' };
    this.state = { address: 'PublicKeyAddressHere' };
  }

  async _sendBitcoin() {
    amount = this.state.amount;
    address = this.state.address;
    var {hash} = await BitcoinWallet.createTransaction(amount, address);
    console.log(hash);
    this.props.navigation.navigate('Home');
  }

  render() {
    return (
      <View>
      <TextInput
        style={{height: 40, borderColor: 'gray', borderWidth: 1}}
        onChangeText={(text) => this.setState({amount})}
        value={this.state.amount}
      />
      <TextInput
        style={{height: 40, borderColor: 'gray', borderWidth: 1}}
        onChangeText={(text) => this.setState({address})}
        value={this.state.address}
      />
      <Button title='Submit' onPress={() => this._sendBitcoin()}/>
      </View>
    );
  }
}
