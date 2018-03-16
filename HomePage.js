'use-strict';

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Button
} from 'react-native';

class TransactionList extends Component<{}> {
  render() {
    return (
      <Text>Placeholder</Text>
    );
  }
}

export default class HomePage extends Component<{}> {
  static navigationOptions = {
    title: 'addr',
  };

  _recvSubmit = () => {
    this.props.navigation.navigate('Receive')
  }

  _sendSubmit = () => {
    this.props.navigation.navigate('Send', null)
  }

  render() {
    return (
      <View style={styles.container}>
        <TransactionList/>
        <Button title='Receive' onPress={this._recvSubmit}/>
        <Button title='Send' onPress={this._sendSubmit}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
});
