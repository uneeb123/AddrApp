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
      <View style={styles.transactions}>
        <Text>Placeholder</Text>
      </View>
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
    this.props.navigation.navigate('Send')
  }

  render() {
    return (
      <View style={styles.container}>
        <TransactionList/>
        <View style={styles.bottomButtons}>
          <Button 
            title='Receive coins' onPress={this._recvSubmit}/>
          <Button 
            title='Send coins' onPress={this._sendSubmit}/>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  transactions: {
    flex: 1,
    height: 100,
  },
  bottomButtons: {
    alignSelf: 'stretch',
    alignItems: 'center',
    justifyContent: 'space-between',
    flexDirection: 'row',
    margin: 2,
  },
});
