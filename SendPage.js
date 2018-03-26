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
    defaultState = { amount: '0.00', address: 'PublicKeyAddressHere' };
    this.state = defaultState;
  }

  async _sendBitcoin() {
    amount = this.state.amount;
    address = this.state.address;
    var {hash} = await BitcoinWallet.createTransaction(amount, address);
    console.log(hash);
    this.props.navigation.navigate('Home');
  }

  _setAmount(amount) {
    this.setState((previousState) => {
      previousState.amount = amount;
      return previousState;
    });
  }

  _setAddress(address) {
    this.setState((previousState) => {
      previousState.address = address;
      return previousState;
    });
  }

  render() {
    return (
      <View>
        <TextInput
          style={{height: 40}}
          onChangeText={(text) => this._setAmount(text)}
          value={this.state.amount}
        />
        <TextInput
          style={{height: 40}}
          onChangeText={(text) => this._setAddress(text)}
          value={this.state.address}
        />
        <Button title='Submit' onPress={() => this._sendBitcoin()}/>
      </View>
    );
  }
}
