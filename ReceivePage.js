'use-strict';

import React, { Component } from 'react';
import {
  View,
  Text
} from 'react-native';

import BitcoinWallet from './BitcoinWallet'

export default class ReceivePage extends Component<{}> {
  static navigationOptions = {
    title: 'Receive coins',
  };

  constructor(props) {
    super(props);
    this.state = {port: 0};

    this.fetchPort();
  }

  async fetchPort() {
    try {
      var {port} = await BitcoinWallet.port();
      this.setState((previousState)=> {
        return { port: port };
      });
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    let port = this.state.port;
    return (
      <Text>{port}</Text>
    );
  }
}
