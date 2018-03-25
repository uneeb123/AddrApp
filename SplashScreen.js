'use-strict';

import React, { Component } from 'react';
import {
  View,
  Button,
  StyleSheet,
  ActivityIndicator,
  Text,
  DeviceEventEmitter,
} from 'react-native';
import BitcoinWallet from './BitcoinWallet'

export default class SplashScreen extends Component<{}> {
  constructor(props) {
    super(props)

    this._startSync();
  }

  async _startSync() {
    await BitcoinWallet.initiateWallet();
  }

  _goToHome = () => {
    this.props.navigation.navigate('Home');
  }

  componentDidMount() {
    DeviceEventEmitter.addListener('syncCompleted', (e) => {
      this._goToHome();
    });
  }

  render() {
    return (
      <View style={styles.container}>
        <ActivityIndicator size="large" color="#298e82" />
        <View style={{height: 50}} />
        <Text>Syncing up blockchain...</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  }
});
