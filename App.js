'use-strict';

import HomePage from './HomePage';
import ReceivePage from './ReceivePage';
import SendPage from './SendPage';

import React, { Component } from 'react';
import {
  StackNavigator,
} from 'react-navigation';

const App = StackNavigator({
  Home: { screen: HomePage },
  Receive: { screen: ReceivePage },
  Send: { screen: SendPage },
});
export default App;
