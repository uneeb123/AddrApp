'use-strict';

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Button,
  FlatList
} from 'react-native';

class Transaction extends Component<{}> {
  render() {
    return (
      <View style={styles.transaction_container}>
        <View style={styles.date_container}>
          <Text>{this.props.item.date}</Text>
        </View>
        <View style={styles.addr_container}>
          <Text>{this.props.item.addr}</Text>
        </View>
        <View style={styles.prince_container}>
          <Text>{this.props.item.amount_bitcoin + "\n"}</Text>
          <Text>{"$" + this.props.item.amount_dollar}</Text>
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
  transaction_container: {
    flexDirection: 'row',
    backgroundColor: 'white',
    margin: 2,
    elevation: 2,
  },
  date_container: {
    flex: 1,
  },
  addr_container: {
    flex: 2,
  },
  price_container: {
    flex: 1,
  }
});
