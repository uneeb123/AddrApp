'use-strict';

import React, { Component } from 'react';
import {
  Text
} from 'react-native';

export default class SendPage extends Component<{}> {
  static navigationOptions = {
    title: 'Send coins',
  };

  render() {
    return (
      <Text style={{fontFamily: 'pacifico'}}>SendPage</Text>
    );
  }
}
